package com.dinnerspinner.app.ui.meals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dinnerspinner.app.data.local.MealDatabase
import com.dinnerspinner.app.data.model.Complexity
import com.dinnerspinner.app.data.model.CookingTime
import com.dinnerspinner.app.data.model.Meal
import com.dinnerspinner.app.data.model.Nutrition
import com.dinnerspinner.app.data.model.Price
import com.dinnerspinner.app.data.model.Protein
import com.dinnerspinner.app.data.model.Staple
import com.dinnerspinner.app.data.repository.MealRepository
import com.dinnerspinner.app.data.static.CommonDish
import com.dinnerspinner.app.data.static.CommonDishes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddEditMealState(
    val name: String = "",
    val complexity: Complexity? = null,
    val price: Price? = null,
    val protein: Protein? = null,
    val staple: Staple? = null,
    val nutrition: Nutrition? = null,
    val cookingTime: CookingTime? = null,
    val recipe: String = "",
    val isFavorite: Boolean = false,
    val suggestions: List<CommonDish> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val isAddedAndContinue: Boolean = false,
    val nameError: Boolean = false
)

class AddEditMealViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MealRepository(MealDatabase.getInstance(application).mealDao())

    private val _state = MutableStateFlow(AddEditMealState())
    val state: StateFlow<AddEditMealState> = _state

    private var existingMealId: Long? = null
    private var originalCreatedAt: Long = System.currentTimeMillis()

    fun loadMeal(mealId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val meal = repository.getMealById(mealId)
            if (meal != null) {
                existingMealId = meal.id
                originalCreatedAt = meal.createdAt
                _state.update {
                    it.copy(
                        name = meal.name,
                        complexity = meal.complexity,
                        price = meal.price,
                        protein = meal.protein,
                        staple = meal.staple,
                        nutrition = meal.nutrition,
                        cookingTime = meal.cookingTime,
                        recipe = meal.recipe ?: "",
                        isFavorite = meal.isFavorite,
                        isLoading = false
                    )
                }
            } else {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onNameChange(name: String) {
        _state.update {
            it.copy(
                name = name,
                nameError = false,
                suggestions = CommonDishes.search(name)
            )
        }
    }

    fun applySuggestion(dish: CommonDish) {
        _state.update {
            it.copy(
                name = dish.name,
                complexity = dish.complexity ?: it.complexity,
                price = dish.price ?: it.price,
                protein = dish.protein ?: it.protein,
                staple = dish.staple ?: it.staple,
                nutrition = dish.nutrition ?: it.nutrition,
                cookingTime = dish.cookingTime ?: it.cookingTime,
                suggestions = emptyList()
            )
        }
    }

    fun dismissSuggestions() { _state.update { it.copy(suggestions = emptyList()) } }

    fun onComplexityChange(value: Complexity?) { _state.update { it.copy(complexity = value) } }
    fun onPriceChange(value: Price?) { _state.update { it.copy(price = value) } }
    fun onProteinChange(value: Protein?) { _state.update { it.copy(protein = value) } }
    fun onStapleChange(value: Staple?) { _state.update { it.copy(staple = value) } }
    fun onNutritionChange(value: Nutrition?) { _state.update { it.copy(nutrition = value) } }
    fun onCookingTimeChange(value: CookingTime?) { _state.update { it.copy(cookingTime = value) } }
    fun onRecipeChange(value: String) { _state.update { it.copy(recipe = value) } }
    fun onFavoriteChange(value: Boolean) { _state.update { it.copy(isFavorite = value) } }

    fun save() {
        val s = _state.value
        if (s.name.isBlank()) {
            _state.update { it.copy(nameError = true) }
            return
        }
        viewModelScope.launch {
            val meal = buildMeal(s)
            if (existingMealId != null) repository.updateMeal(meal)
            else repository.insertMeal(meal)
            _state.update { it.copy(isSaved = true) }
        }
    }

    fun saveAndAddAnother() {
        val s = _state.value
        if (s.name.isBlank()) {
            _state.update { it.copy(nameError = true) }
            return
        }
        viewModelScope.launch {
            repository.insertMeal(buildMeal(s))
            _state.value = AddEditMealState(isAddedAndContinue = true)
            originalCreatedAt = System.currentTimeMillis()
        }
    }

    fun resetContinueFlag() {
        _state.update { it.copy(isAddedAndContinue = false) }
    }

    private fun buildMeal(s: AddEditMealState) = Meal(
        id = existingMealId ?: 0L,
        name = s.name.trim(),
        complexity = s.complexity,
        price = s.price,
        protein = s.protein,
        staple = s.staple,
        nutrition = s.nutrition,
        cookingTime = s.cookingTime,
        recipe = s.recipe.ifBlank { null },
        isFavorite = s.isFavorite,
        createdAt = originalCreatedAt
    )
}
