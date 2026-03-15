package com.dinnerspinner.app.ui.navigation

sealed class Screen(val route: String) {
    object Spinner : Screen("spinner")
    object MyMeals : Screen("my_meals")
    object AddMealPicker : Screen("add_meal_picker")
    object AddEditMeal : Screen("add_edit_meal?mealId={mealId}") {
        fun createRoute(mealId: Long? = null) =
            if (mealId != null) "add_edit_meal?mealId=$mealId" else "add_edit_meal"
    }
    object MealPlan : Screen("meal_plan")
}
