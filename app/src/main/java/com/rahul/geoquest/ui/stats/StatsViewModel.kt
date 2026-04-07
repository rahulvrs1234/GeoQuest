package com.rahul.geoquest.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.geoquest.data.repository.GeoQuestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatsViewModel(
    private val repository: GeoQuestRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(isLoading = true, errorMessage = null) }

            runCatching { repository.getStatsSummary() }
                .onSuccess { summary ->
                    _uiState.value = StatsUiState(
                        isLoading = false,
                        stats = summary.stats,
                        recentQuizzes = summary.recentQuizzes,
                    )
                }
                .onFailure { error ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Unable to load your progress history right now.",
                        )
                    }
                }
        }
    }
}
