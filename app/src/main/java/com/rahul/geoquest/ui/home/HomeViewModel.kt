package com.rahul.geoquest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.geoquest.data.preferences.UserPreferencesRepository
import com.rahul.geoquest.data.repository.GeoQuestRepository
import com.rahul.geoquest.util.toDisplayDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: GeoQuestRepository,
    private val preferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh(forceSync: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(isLoading = true, errorMessage = null) }

            runCatching {
                if (forceSync) {
                    repository.syncCountries().getOrThrow()
                }

                val preferences = preferencesRepository.settingsFlow.first()
                val dashboard = repository.getDashboardSummary()

                HomeUiState(
                    isLoading = false,
                    difficultyLabel = preferences.difficulty.label,
                    streakDays = dashboard.stats.currentStreakDays,
                    quizzesCompleted = dashboard.stats.totalQuizzes,
                    accuracyPercentage = dashboard.stats.accuracyPercentage,
                    countryCount = dashboard.countryCount,
                    latestSyncLabel = dashboard.latestSyncMillis?.toDisplayDateTime() ?: "Not synced yet",
                )
            }.onSuccess { state ->
                _uiState.value = state
            }.onFailure { error ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Unable to load your learning dashboard right now.",
                    )
                }
            }
        }
    }
}

