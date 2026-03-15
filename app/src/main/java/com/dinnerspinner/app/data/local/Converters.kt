package com.dinnerspinner.app.data.local

import androidx.room.TypeConverter
import com.dinnerspinner.app.data.model.Complexity
import com.dinnerspinner.app.data.model.CookingTime
import com.dinnerspinner.app.data.model.Nutrition
import com.dinnerspinner.app.data.model.Price
import com.dinnerspinner.app.data.model.Protein
import com.dinnerspinner.app.data.model.Staple

class Converters {
    @TypeConverter fun fromComplexity(v: Complexity?): String? = v?.name
    @TypeConverter fun toComplexity(v: String?): Complexity? = v?.let { Complexity.valueOf(it) }

    @TypeConverter fun fromPrice(v: Price?): String? = v?.name
    @TypeConverter fun toPrice(v: String?): Price? = v?.let { Price.valueOf(it) }

    @TypeConverter fun fromProtein(v: Protein?): String? = v?.name
    @TypeConverter fun toProtein(v: String?): Protein? = v?.let { Protein.valueOf(it) }

    @TypeConverter fun fromStaple(v: Staple?): String? = v?.name
    @TypeConverter fun toStaple(v: String?): Staple? = v?.let { Staple.valueOf(it) }

    @TypeConverter fun fromNutrition(v: Nutrition?): String? = v?.name
    @TypeConverter fun toNutrition(v: String?): Nutrition? = v?.let { Nutrition.valueOf(it) }

    @TypeConverter fun fromCookingTime(v: CookingTime?): String? = v?.name
    @TypeConverter fun toCookingTime(v: String?): CookingTime? = v?.let { CookingTime.valueOf(it) }
}
