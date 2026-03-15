package com.dinnerspinner.app.ui.meals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dinnerspinner.app.data.local.MealDatabase
import com.dinnerspinner.app.data.model.Meal
import com.dinnerspinner.app.data.repository.MealRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MealSortOrder { NAME, DATE_ADDED, LAST_EATEN }

data class MyMealsUiState(
    val meals: List<Meal> = emptyList(),
    val searchQuery: String = "",
    val sortOrder: MealSortOrder = MealSortOrder.NAME,
    val recentlyDeleted: Meal? = null
)

class MyMealsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MealRepository(MealDatabase.getInstance(application).mealDao())

    val searchQuery = MutableStateFlow("")
    val sortOrder = MutableStateFlow(MealSortOrder.NAME)
    private val recentlyDeleted = MutableStateFlow<Meal?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = combine(
        repository.getAllMeals(),
        searchQuery,
        sortOrder,
        recentlyDeleted
    ) { meals, query, sort, deleted ->
        val filtered = if (query.isBlank()) meals
        else meals.filter { it.name.contains(query, ignoreCase = true) }

        val sorted = when (sort) {
            MealSortOrder.NAME -> filtered.sortedBy { it.name }
            MealSortOrder.DATE_ADDED -> filtered.sortedByDescending { it.createdAt }
            MealSortOrder.LAST_EATEN -> filtered.sortedByDescending { it.lastEatenAt ?: 0L }
        }

        MyMealsUiState(
            meals = sorted,
            searchQuery = query,
            sortOrder = sort,
            recentlyDeleted = deleted
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MyMealsUiState())

    fun setSearchQuery(query: String) { searchQuery.value = query }
    fun setSortOrder(order: MealSortOrder) { sortOrder.value = order }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            repository.deleteMeal(meal)
            recentlyDeleted.value = meal
        }
    }

    fun undoDelete() {
        viewModelScope.launch {
            recentlyDeleted.value?.let { repository.insertMeal(it) }
            recentlyDeleted.value = null
        }
    }

    fun clearRecentlyDeleted() { recentlyDeleted.value = null }

    fun toggleFavorite(meal: Meal) {
        viewModelScope.launch { repository.setFavorite(meal.id, !meal.isFavorite) }
    }
}
