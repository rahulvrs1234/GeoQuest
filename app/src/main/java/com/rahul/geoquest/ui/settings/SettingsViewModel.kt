package com.rahul.geoquest.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.geoquest.data.preferences.UserPreferencesRepository
import com.rahul.geoquest.domain.model.AppThemeMode
import com.rahul.geoquest.domain.model.Difficulty
import com.rahul.geoquest.worker.WorkScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val workScheduler: WorkScheduler,
) : ViewModel() {
    val uiState = preferencesRepository.settingsFlow
        .map { preferences ->
            SettingsUiState(
                isLoading = false,
                difficulty = preferences.difficulty,
                dailyRemindersEnabled = preferences.dailyRemindersEnabled,
                showRegionHints = preferences.showRegionHints,
                themeMode = preferences.themeMode,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState(),
        )

    fun setDifficulty(difficulty: Difficulty) {
        viewModelScope.launch {
            preferencesRepository.setDifficulty(difficulty)
        }
    }

    fun setDailyRemindersEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setDailyRemindersEnabled(enabled)
            if (enabled) {
                workScheduler.scheduleDailyReminder()
            } else {
                workScheduler.cancelDailyReminder()
            }
        }
    }

    fun setShowRegionHints(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setShowRegionHints(enabled)
        }
    }

    fun setThemeMode(themeMode: AppThemeMode) {
        viewModelScope.launch {
            preferencesRepository.setThemeMode(themeMode)
        }
    }
}
