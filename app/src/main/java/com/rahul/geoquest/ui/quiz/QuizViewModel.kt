package com.rahul.geoquest.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.geoquest.data.preferences.UserPreferencesRepository
import com.rahul.geoquest.data.repository.GeoQuestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuizViewModel(
    private val repository: GeoQuestRepository,
    private val preferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState = _uiState.asStateFlow()

    init {
        startNewQuiz()
    }

    fun startNewQuiz() {
        viewModelScope.launch {
            _uiState.value = QuizUiState(isLoading = true)

            runCatching {
                val preferences = preferencesRepository.settingsFlow.first()
                val questions = repository.getQuizQuestions(preferences.difficulty)

                QuizUiState(
                    isLoading = false,
                    difficulty = preferences.difficulty,
                    showRegionHints = preferences.showRegionHints,
                    hintRevealed = false,
                    questions = questions,
                    startedAtMillis = System.currentTimeMillis(),
                    errorMessage = if (questions.isEmpty()) "Not enough country data is available yet." else null,
                )
            }.onSuccess { state ->
                _uiState.value = state
            }.onFailure { error ->
                _uiState.value = QuizUiState(
                    isLoading = false,
                    errorMessage = error.message ?: "Unable to create a quiz round right now.",
                )
            }
        }
    }

    fun selectAnswer(answer: String) {
        val currentState = _uiState.value
        val currentQuestion = currentState.currentQuestion ?: return

        if (currentState.selectedAnswer != null || currentState.quizComplete) {
            return
        }

        _uiState.update { state ->
            state.copy(
                selectedAnswer = answer,
                score = state.score + if (answer == currentQuestion.correctAnswer) 1 else 0,
            )
        }
    }

    fun moveToNextQuestion() {
        val currentState = _uiState.value
        if (currentState.selectedAnswer == null) {
            return
        }

        if (currentState.currentIndex == currentState.questions.lastIndex) {
            finishQuiz()
            return
        }

        _uiState.update { state ->
            state.copy(
                currentIndex = state.currentIndex + 1,
                hintRevealed = false,
                selectedAnswer = null,
            )
        }
    }

    fun revealHint() {
        _uiState.update { state ->
            if (!state.showRegionHints || state.hintRevealed) {
                state
            } else {
                state.copy(hintRevealed = true)
            }
        }
    }

    private fun finishQuiz() {
        viewModelScope.launch {
            val state = _uiState.value
            val durationSeconds = ((System.currentTimeMillis() - state.startedAtMillis) / 1000L).coerceAtLeast(1L)

            repository.recordQuizAttempt(
                score = state.score,
                totalQuestions = state.questions.size,
                difficulty = state.difficulty,
                durationSeconds = durationSeconds,
            )

            _uiState.update { current ->
                current.copy(
                    quizComplete = true,
                    selectedAnswer = null,
                )
            }
        }
    }
}
