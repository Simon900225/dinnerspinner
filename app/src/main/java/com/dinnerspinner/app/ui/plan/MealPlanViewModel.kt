package com.dinnerspinner.app.ui.plan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dinnerspinner.app.data.local.MealDatabase
import com.dinnerspinner.app.data.model.Complexity
import com.dinnerspinner.app.data.model.CookingTime
import com.dinnerspinner.app.data.model.Meal
import com.dinnerspinner.app.data.model.MealPlan
import com.dinnerspinner.app.data.model.MealPlanEntry
import com.dinnerspinner.app.data.model.Nutrition
import com.dinnerspinner.app.data.model.Price
import com.dinnerspinner.app.data.model.Protein
import com.dinnerspinner.app.data.model.Staple
import com.dinnerspinner.app.data.preferences.UserPreferencesRepository
import com.dinnerspinner.app.data.repository.MealRepository
import com.dinnerspinner.app.ui.components.MealFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class MealPlanUiState(
    val allMeals: List<Meal> = emptyList(),
    val planEntries: List<MealPlanEntry> = emptyList(),
    val filter: MealFilter = MealFilter(),
    val isFilterExpanded: Boolean = false,
    val isGenerated: Boolean = false
)

private val DAY_NAMES = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

class MealPlanViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MealRepository(MealDatabase.getInstance(application).mealDao())
    private val prefsRepo = UserPreferencesRepository(application)

    private val _planEntries = MutableStateFlow<List<MealPlanEntry>>(emptyList())
    private val _filter = MutableStateFlow(MealFilter())
    private val _isFilterExpanded = MutableStateFlow(false)
    private val _isGenerated = MutableStateFlow(false)

    val uiState: StateFlow<MealPlanUiState> = combine(
        repository.getAllMeals(),
        _planEntries,
        _filter,
        _isFilterExpanded,
        _isGenerated
    ) { meals, entries, filter, expanded, generated ->
        MealPlanUiState(
            allMeals = meals,
            planEntries = entries,
            filter = filter,
            isFilterExpanded = expanded,
            isGenerated = generated
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MealPlanUiState())

    fun toggleFilterExpanded() { _isFilterExpanded.update { !it } }

    fun setMaxComplexity(value: Complexity?) { _filter.update { it.copy(maxComplexity = value) } }
    fun setMaxPrice(value: Price?) { _filter.update { it.copy(maxPrice = value) } }
    fun toggleProtein(protein: Protein) {
        _filter.update {
            val updated = if (protein in it.proteins) it.proteins - protein else it.proteins + protein
            it.copy(proteins = updated)
        }
    }
    fun toggleStaple(staple: Staple) {
        _filter.update {
            val updated = if (staple in it.staples) it.staples - staple else it.staples + staple
            it.copy(staples = updated)
        }
    }
    fun setMaxNutrition(value: Nutrition?) { _filter.update { it.copy(maxNutrition = value) } }
    fun setMaxCookingTime(value: CookingTime?) { _filter.update { it.copy(maxCookingTime = value) } }
    fun setAvoidRepeats(value: Boolean) { _filter.update { it.copy(avoidRepeats = value) } }
    fun setExcludeRecentlyEaten(value: Boolean) { _filter.update { it.copy(excludeRecentlyEaten = value) } }
    fun setExcludeWithinDays(value: Int) { _filter.update { it.copy(excludeWithinDays = value) } }

    fun generatePlan() {
        viewModelScope.launch {
            val filter = _filter.value
            val allMeals = uiState.value.allMeals
            val lockedEntries = _planEntries.value.filter { it.isLocked }

            val recentlyEatenIds = if (filter.excludeRecentlyEaten) {
                repository.getMealIdsEatenSince(filter.excludeWithinDays).toSet()
            } else emptySet()

            val lockedDays = lockedEntries.map { it.dayOfWeek }.toSet()
            val filtered = applyPlanFilters(allMeals, filter, recentlyEatenIds)
            val pool = buildWeightedPool(filtered)

            val newEntries = mutableListOf<MealPlanEntry>()
            val usedMealIds = lockedEntries.map { it.mealId }.toMutableSet()
            val shuffled = pool.shuffled().toMutableList()

            for (day in 1..7) {
                val locked = lockedEntries.find { it.dayOfWeek == day }
                if (locked != null) {
                    newEntries.add(locked)
                    continue
                }

                val candidate = if (filter.avoidRepeats) {
                    shuffled.firstOrNull { it.id !in usedMealIds }
                        ?: shuffled.firstOrNull() // fall back to repeats if pool exhausted
                } else {
                    shuffled.randomOrNull()
                }

                if (candidate != null) {
                    newEntries.add(MealPlanEntry(day, candidate.id, candidate.name))
                    usedMealIds.add(candidate.id)
                    shuffled.remove(candidate)
                    if (shuffled.isEmpty()) shuffled.addAll(pool.shuffled())
                }
            }

            _planEntries.value = newEntries.sortedBy { it.dayOfWeek }
            _isGenerated.value = true

            val plan = MealPlan(entriesJson = Json.encodeToString(newEntries))
            repository.saveMealPlan(plan)
        }
    }

    fun rerollDay(dayOfWeek: Int) {
        viewModelScope.launch {
            val filter = _filter.value
            val allMeals = uiState.value.allMeals
            val currentEntries = _planEntries.value
            val usedMealIds = currentEntries
                .filter { it.dayOfWeek != dayOfWeek }
                .map { it.mealId }
                .toSet()

            val recentlyEatenIds = if (filter.excludeRecentlyEaten) {
                repository.getMealIdsEatenSince(filter.excludeWithinDays).toSet()
            } else emptySet()

            val filtered = applyPlanFilters(allMeals, filter, recentlyEatenIds)
            val candidate = filtered
                .filter { if (filter.avoidRepeats) it.id !in usedMealIds else true }
                .randomOrNull()
                ?: filtered.randomOrNull()

            if (candidate != null) {
                _planEntries.update { entries ->
                    entries.map { entry ->
                        if (entry.dayOfWeek == dayOfWeek)
                            MealPlanEntry(dayOfWeek, candidate.id, candidate.name)
                        else entry
                    }
                }
            }
        }
    }

    fun toggleLockDay(dayOfWeek: Int) {
        _planEntries.update { entries ->
            entries.map { entry ->
                if (entry.dayOfWeek == dayOfWeek) entry.copy(isLocked = !entry.isLocked)
                else entry
            }
        }
    }

    fun getShareText(): String {
        val entries = _planEntries.value
        if (entries.isEmpty()) return ""
        val lines = entries.joinToString("\n") { entry ->
            "${DAY_NAMES[entry.dayOfWeek - 1]}: ${entry.mealName}"
        }
        return "My Dinner Plan\n${"-".repeat(30)}\n$lines"
    }

    private fun applyPlanFilters(
        meals: List<Meal>,
        filter: MealFilter,
        recentlyEatenIds: Set<Long>
    ): List<Meal> = meals.filter { meal ->
        if (filter.excludeRecentlyEaten && meal.id in recentlyEatenIds) return@filter false
        filter.matches(meal)
    }

    private fun buildWeightedPool(meals: List<Meal>): List<Meal> {
        return meals.flatMap { meal ->
            if (meal.isFavorite) listOf(meal, meal) else listOf(meal)
        }
    }
}
