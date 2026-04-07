package com.rahul.geoquest.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rahul.geoquest.domain.model.AppThemeMode
import com.rahul.geoquest.domain.model.Difficulty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.geoQuestPreferences: DataStore<Preferences> by preferencesDataStore(name = "geoquest_preferences")

data class UserPreferences(
    val difficulty: Difficulty = Difficulty.Medium,
    val dailyRemindersEnabled: Boolean = false,
    val showRegionHints: Boolean = true,
    val themeMode: AppThemeMode = AppThemeMode.System,
)

class UserPreferencesRepository(
    private val context: Context,
) {
    val settingsFlow: Flow<UserPreferences> =
        context.geoQuestPreferences.data.map { preferences ->
            UserPreferences(
                difficulty = Difficulty.fromValue(preferences[Keys.Difficulty] ?: Difficulty.Medium.name),
                dailyRemindersEnabled = preferences[Keys.DailyReminders] ?: false,
                showRegionHints = preferences[Keys.ShowRegionHints] ?: true,
                themeMode = AppThemeMode.fromValue(preferences[Keys.ThemeMode] ?: AppThemeMode.System.name),
            )
        }

    suspend fun setDifficulty(difficulty: Difficulty) {
        context.geoQuestPreferences.edit { preferences ->
            preferences[Keys.Difficulty] = difficulty.name
        }
    }

    suspend fun setDailyRemindersEnabled(enabled: Boolean) {
        context.geoQuestPreferences.edit { preferences ->
            preferences[Keys.DailyReminders] = enabled
        }
    }

    suspend fun setShowRegionHints(enabled: Boolean) {
        context.geoQuestPreferences.edit { preferences ->
            preferences[Keys.ShowRegionHints] = enabled
        }
    }

    suspend fun setThemeMode(themeMode: AppThemeMode) {
        context.geoQuestPreferences.edit { preferences ->
            preferences[Keys.ThemeMode] = themeMode.name
        }
    }

    private object Keys {
        val Difficulty = stringPreferencesKey("difficulty")
        val DailyReminders = booleanPreferencesKey("daily_reminders")
        val ShowRegionHints = booleanPreferencesKey("show_region_hints")
        val ThemeMode = stringPreferencesKey("theme_mode")
    }
}
