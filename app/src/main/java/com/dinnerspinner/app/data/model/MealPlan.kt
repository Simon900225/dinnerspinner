package com.dinnerspinner.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class MealPlanEntry(
    val dayOfWeek: Int, // 1=Mon, 7=Sun
    val mealId: Long,
    val mealName: String,
    val isLocked: Boolean = false
)

@Entity(tableName = "meal_plans")
data class MealPlan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val entriesJson: String = "[]"
)
