package com.dinnerspinner.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Complexity(val label: String) {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard")
}

enum class Price(val label: String) {
    CHEAP("Cheap"),
    MEDIUM("Medium"),
    EXPENSIVE("Expensive")
}

enum class Protein(val label: String) {
    FISH("Fish"),
    MEAT("Meat"),
    VEGETARIAN("Vegetarian"),
    VEGAN("Vegan")
}

enum class Staple(val label: String) {
    POTATO("Potato"),
    PASTA("Pasta"),
    RICE("Rice"),
    BREAD("Bread"),
    NONE("No staple")
}

enum class Nutrition(val label: String) {
    HEALTHY("Healthy"),
    OK("Ok"),
    JUNK("Junk")
}

enum class CookingTime(val label: String) {
    QUICK("Quick (<15 min)"),
    MEDIUM("Medium (15-45 min)"),
    LONG("Long (>45 min)")
}

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val complexity: Complexity? = null,
    val price: Price? = null,
    val protein: Protein? = null,
    val staple: Staple? = null,
    val nutrition: Nutrition? = null,
    val cookingTime: CookingTime? = null,
    val recipe: String? = null,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastEatenAt: Long? = null
)
