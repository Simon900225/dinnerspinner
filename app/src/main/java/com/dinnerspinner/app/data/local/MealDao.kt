package com.dinnerspinner.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dinnerspinner.app.data.model.Meal
import com.dinnerspinner.app.data.model.MealPlan
import com.dinnerspinner.app.data.model.SpinHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Query("SELECT * FROM meals ORDER BY name ASC")
    fun getAllMeals(): Flow<List<Meal>>

    @Query("SELECT * FROM meals WHERE id = :id")
    suspend fun getMealById(id: Long): Meal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal): Long

    @Update
    suspend fun updateMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("UPDATE meals SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Query("UPDATE meals SET lastEatenAt = :timestamp WHERE id = :id")
    suspend fun updateLastEaten(id: Long, timestamp: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpinHistory(entry: SpinHistory)

    @Query("SELECT * FROM spin_history ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentSpinHistory(limit: Int = 50): List<SpinHistory>

    @Query("SELECT mealId FROM spin_history WHERE timestamp > :since")
    suspend fun getMealIdsEatenSince(since: Long): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealPlan(plan: MealPlan): Long

    @Query("SELECT * FROM meal_plans ORDER BY createdAt DESC LIMIT 1")
    fun getLatestMealPlan(): Flow<MealPlan?>

    @Query("SELECT * FROM meal_plans ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestMealPlanOnce(): MealPlan?

    @Query("DELETE FROM meal_plans WHERE id NOT IN (SELECT id FROM meal_plans ORDER BY createdAt DESC LIMIT 5)")
    suspend fun pruneOldMealPlans()
}
