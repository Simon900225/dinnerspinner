package com.dinnerspinner.app.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

data class AppPreferences(
    val excludeRecentlyEaten: Boolean = false,
    val excludeWithinDays: Int = 7,
    val favoritesBoost: Boolean = true
)

class UserPreferencesRepository(private val context: Context) {
    private object Keys {
        val EXCLUDE_RECENTLY_EATEN = booleanPreferencesKey("exclude_recently_eaten")
        val EXCLUDE_WITHIN_DAYS = intPreferencesKey("exclude_within_days")
        val FAVORITES_BOOST = booleanPreferencesKey("favorites_boost")
    }

    val preferences: Flow<AppPreferences> = context.dataStore.data.map { prefs ->
        AppPreferences(
            excludeRecentlyEaten = prefs[Keys.EXCLUDE_RECENTLY_EATEN] ?: false,
            excludeWithinDays = prefs[Keys.EXCLUDE_WITHIN_DAYS] ?: 7,
            favoritesBoost = prefs[Keys.FAVORITES_BOOST] ?: true
        )
    }

    suspend fun setExcludeRecentlyEaten(value: Boolean) {
        context.dataStore.edit { it[Keys.EXCLUDE_RECENTLY_EATEN] = value }
    }

    suspend fun setExcludeWithinDays(value: Int) {
        context.dataStore.edit { it[Keys.EXCLUDE_WITHIN_DAYS] = value }
    }

    suspend fun setFavoritesBoost(value: Boolean) {
        context.dataStore.edit { it[Keys.FAVORITES_BOOST] = value }
    }
}
