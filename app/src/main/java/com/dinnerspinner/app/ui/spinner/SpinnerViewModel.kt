package com.dinnerspinner.app.ui.spinner

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dinnerspinner.app.data.local.MealDatabase
import com.dinnerspinner.app.data.model.CookingTime
import com.dinnerspinner.app.data.model.Meal
import com.dinnerspinner.app.data.model.Nutrition
import com.dinnerspinner.app.data.model.Price
import com.dinnerspinner.app.data.model.Protein
import com.dinnerspinner.app.data.preferences.AppPreferences
import com.dinnerspinner.app.data.preferences.UserPreferencesRepository
import com.dinnerspinner.app.data.repository.MealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SpinState { IDLE, SPINNING, RESULT }

data class SpinFilter(
    val quickOnly: Boolean = false,
    val meatOnly: Boolean = false,
    val fishOnly: Boolean = false,
    val cheapOnly: Boolean = false,
    val healthyOnly: Boolean = false,
    val vegetarianOnly: Boolean = false,
)

data class SpinnerUiState(
    val allMeals: List<Meal> = emptyList(),
    val filteredMeals: List<Meal> = emptyList(),
    val spinState: SpinState = SpinState.IDLE,
    val selectedMeal: Meal? = null,
    val rejectedMealIds: Set<Long> = emptySet(),
    val filter: SpinFilter = SpinFilter(),
    val preferences: AppPreferences = AppPreferences()
)

class SpinnerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MealRepository(MealDatabase.getInstance(application).mealDao())
    private val prefsRepo = UserPreferencesRepository(application)

    private val _spinState = MutableStateFlow(SpinState.IDLE)
    private val _selectedMeal = MutableStateFlow<Meal?>(null)
    private val _rejectedMealIds = MutableStateFlow<Set<Long>>(emptySet())
    private val _filter = MutableStateFlow(SpinFilter())

    val uiState: StateFlow<SpinnerUiState> = combine(
        repository.getAllMeals(),
        _spinState,
        _selectedMeal,
        _rejectedMealIds,
        _filter,
        prefsRepo.preferences
    ) { params ->
        @Suppress("UNCHECKED_CAST")
        val allMeals = params[0] as List<Meal>
        val spinState = params[1] as SpinState
        val selectedMeal = params[2] as Meal?
        val rejectedIds = params[3] as Set<Long>
        val filter = params[4] as SpinFilter
        val prefs = params[5] as AppPreferences

        val filtered = applyFilters(allMeals, filter, rejectedIds, prefs)
        SpinnerUiState(
            allMeals = allMeals,
            filteredMeals = filtered,
            spinState = spinState,
            selectedMeal = selectedMeal,
            rejectedMealIds = rejectedIds,
            filter = filter,
            preferences = prefs
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SpinnerUiState())

    private fun applyFilters(
        meals: List<Meal>,
        filter: SpinFilter,
        rejectedIds: Set<Long>,
        prefs: AppPreferences
    ): List<Meal> {
        return meals.filter { meal ->
            if (meal.id in rejectedIds) return@filter false
            if (filter.quickOnly && meal.cookingTime != CookingTime.QUICK && meal.cookingTime != null) return@filter false
            if (filter.meatOnly && meal.protein != Protein.MEAT) return@filter false
            if (filter.fishOnly && meal.protein != Protein.FISH) return@filter false
            if (filter.cheapOnly && meal.price != Price.CHEAP && meal.price != null) return@filter false
            if (filter.healthyOnly && meal.nutrition != Nutrition.HEALTHY && meal.nutrition != null) return@filter false
            if (filter.vegetarianOnly && meal.protein != Protein.VEGETARIAN && meal.protein != Protein.VEGAN) return@filter false
            true
        }
    }

    fun setSpinState(state: SpinState) { _spinState.value = state }

    fun pickRandomMeal(): Meal? {
        val state = uiState.value
        val pool = buildWeightedPool(state.filteredMeals, state.preferences.favoritesBoost)
        return pool.randomOrNull()
    }

    private fun buildWeightedPool(meals: List<Meal>, favoritesBoost: Boolean): List<Meal> {
        if (!favoritesBoost) return meals
        return meals.flatMap { meal ->
            if (meal.isFavorite) listOf(meal, meal) else listOf(meal)
        }
    }

    fun onSpinComplete(meal: Meal) {
        _selectedMeal.value = meal
        _spinState.value = SpinState.RESULT
        viewModelScope.launch { repository.recordSpin(meal.id) }
    }

    fun reroll() {
        val current = _selectedMeal.value
        if (current != null) {
            _rejectedMealIds.update { it + current.id }
        }
        _selectedMeal.value = null
        _spinState.value = SpinState.IDLE
    }

    fun resetSession() {
        _rejectedMealIds.value = emptySet()
        _selectedMeal.value = null
        _spinState.value = SpinState.IDLE
    }

    fun toggleQuickFilter() { _filter.update { it.copy(quickOnly = !it.quickOnly) } }
    fun toggleMeatFilter() { _filter.update { it.copy(meatOnly = !it.meatOnly, fishOnly = false, vegetarianOnly = false) } }
    fun toggleFishFilter() { _filter.update { it.copy(fishOnly = !it.fishOnly, meatOnly = false, vegetarianOnly = false) } }
    fun toggleCheapFilter() { _filter.update { it.copy(cheapOnly = !it.cheapOnly) } }
    fun toggleHealthyFilter() { _filter.update { it.copy(healthyOnly = !it.healthyOnly) } }
    fun toggleVegetarianFilter() { _filter.update { it.copy(vegetarianOnly = !it.vegetarianOnly, meatOnly = false, fishOnly = false) } }
}
