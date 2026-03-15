package com.dinnerspinner.app.data.repository

import com.dinnerspinner.app.data.local.MealDao
import com.dinnerspinner.app.data.model.Meal
import com.dinnerspinner.app.data.model.MealPlan
import com.dinnerspinner.app.data.model.SpinHistory
import kotlinx.coroutines.flow.Flow

class MealRepository(private val dao: MealDao) {

    fun getAllMeals(): Flow<List<Meal>> = dao.getAllMeals()

    suspend fun getMealById(id: Long): Meal? = dao.getMealById(id)

    suspend fun insertMeal(meal: Meal): Long = dao.insertMeal(meal)

    suspend fun updateMeal(meal: Meal) = dao.updateMeal(meal)

    suspend fun deleteMeal(meal: Meal) = dao.deleteMeal(meal)

    suspend fun setFavorite(id: Long, isFavorite: Boolean) = dao.setFavorite(id, isFavorite)

    suspend fun updateLastEaten(id: Long) = dao.updateLastEaten(id, System.currentTimeMillis())

    suspend fun recordSpin(mealId: Long) {
        dao.insertSpinHistory(SpinHistory(mealId = mealId))
        dao.updateLastEaten(mealId, System.currentTimeMillis())
    }

    suspend fun getMealIdsEatenSince(daysAgo: Int): List<Long> {
        val since = System.currentTimeMillis() - daysAgo * 24 * 60 * 60 * 1000L
        return dao.getMealIdsEatenSince(since)
    }

    suspend fun saveMealPlan(plan: MealPlan): Long {
        val id = dao.insertMealPlan(plan)
        dao.pruneOldMealPlans()
        return id
    }

    fun getLatestMealPlan(): Flow<MealPlan?> = dao.getLatestMealPlan()

    suspend fun getLatestMealPlanOnce(): MealPlan? = dao.getLatestMealPlanOnce()
}
