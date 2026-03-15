package com.dinnerspinner.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dinnerspinner.app.data.model.Meal
import com.dinnerspinner.app.data.model.MealPlan
import com.dinnerspinner.app.data.model.SpinHistory

@Database(
    entities = [Meal::class, SpinHistory::class, MealPlan::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MealDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile private var INSTANCE: MealDatabase? = null

        fun getInstance(context: Context): MealDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    MealDatabase::class.java,
                    "dinner_spinner.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
