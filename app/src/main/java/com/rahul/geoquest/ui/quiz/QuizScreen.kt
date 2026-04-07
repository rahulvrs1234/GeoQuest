package com.rahul.geoquest.ui.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rahul.geoquest.domain.model.Difficulty
import com.rahul.geoquest.domain.model.QuizQuestion
import com.rahul.geoquest.ui.components.GeoQuestBackground

data class QuizUiState(
    val isLoading: Boolean = true,
    val difficulty: Difficulty = Difficulty.Medium,
    val showRegionHints: Boolean = true,
    val hintRevealed: Boolean = false,
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val score: Int = 0,
    val quizComplete: Boolean = false,
    val startedAtMillis: Long = 0L,
    val errorMessage: String? = null,
) {
    val currentQuestion: QuizQuestion?
        get() = questions.getOrNull(currentIndex)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    uiState: QuizUiState,
    onAnswerSelected: (String) -> Unit,
    onRevealHint: () -> Unit,
    onNextQuestion: () -> Unit,
    onRestartQuiz: () -> Unit,
    onExit: () -> Unit,
    onViewStats: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                title = { Text("Practice round") },
                navigationIcon = {
                    IconButton(onClick = onExit) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                GeoQuestBackground(
                    modifier = Modifier.padding(innerPadding),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Building your next quiz...",
                            modifier = Modifier.padding(top = 16.dp),
                        )
                    }
                }
            }

            uiState.quizComplete -> {
                QuizSummaryContent(
                    uiState = uiState,
                    onRestartQuiz = onRestartQuiz,
                    onViewStats = onViewStats,
                    modifier = Modifier.padding(innerPadding),
                )
            }

            else -> {
                QuizQuestionContent(
                    uiState = uiState,
                    onAnswerSelected = onAnswerSelected,
                    onRevealHint = onRevealHint,
                    onNextQuestion = onNextQuestion,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}

@Composable
private fun QuizQuestionContent(
    uiState: QuizUiState,
    onAnswerSelected: (String) -> Unit,
    onRevealHint: () -> Unit,
    onNextQuestion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val question = uiState.currentQuestion

    if (question == null) {
        GeoQuestBackground(modifier = modifier) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("No quiz questions are available yet.")
                Text(
                    text = uiState.errorMessage ?: "Try syncing the learning content from the home screen first.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        return
    }

    GeoQuestBackground(modifier = modifier) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
        ) {
            val compactMode = maxHeight < 670.dp
            val outerSpacing = if (compactMode) 12.dp else 16.dp
            val cardPadding = if (compactMode) 14.dp else 16.dp
            val imageHeight = if (compactMode) 132.dp else 170.dp
            val answerSpacing = if (compactMode) 10.dp else 12.dp

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(outerSpacing),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AssistChip(
                        onClick = {},
                        label = { Text(uiState.difficulty.label) },
                    )
                    Text(
                        text = "Question ${uiState.currentIndex + 1} of ${uiState.questions.size}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                LinearProgressIndicator(
                    progress = { (uiState.currentIndex + 1).toFloat() / uiState.questions.size.toFloat() },
                    modifier = Modifier.fillMaxWidth(),
                )

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(cardPadding),
                        verticalArrangement = Arrangement.spacedBy(if (compactMode) 10.dp else 12.dp),
                    ) {
                        Text(
                            text = question.prompt,
                            style = if (compactMode) {
                                MaterialTheme.typography.titleLarge
                            } else {
                                MaterialTheme.typography.headlineSmall
                            },
                        )

                        question.imageUrl?.let { imageUrl ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = "Country flag question",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(imageHeight)
                                        .padding(10.dp),
                                    contentScale = ContentScale.Fit,
                                )
                            }
                        }

                        if (uiState.showRegionHints && !question.supportingText.isNullOrBlank()) {
                            if (uiState.hintRevealed) {
                                AssistChip(
                                    onClick = {},
                                    label = {
                                        Text(
                                            text = question.supportingText.orEmpty(),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    },
                                )
                            } else {
                                AssistChip(
                                    onClick = onRevealHint,
                                    label = { Text("Hint") },
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(answerSpacing),
                ) {
                    question.options.forEachIndexed { index, option ->
                        AnswerOption(
                            optionIndex = index,
                            option = option,
                            selectedAnswer = uiState.selectedAnswer,
                            correctAnswer = question.correctAnswer,
                            onClick = { onAnswerSelected(option) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        )
                    }
                }

                uiState.selectedAnswer?.let { selected ->
                    val answeredCorrectly = selected == question.correctAnswer
                    Text(
                        text = if (answeredCorrectly) {
                            "Correct. ${question.correctAnswer} is the right answer."
                        } else {
                            "Not quite. The correct answer is ${question.correctAnswer}."
                        },
                        color = if (answeredCorrectly) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Button(
                        onClick = onNextQuestion,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = if (uiState.currentIndex == uiState.questions.lastIndex) {
                                "See results"
                            } else {
                                "Next question"
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnswerOption(
    optionIndex: Int,
    option: String,
    selectedAnswer: String?,
    correctAnswer: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val answerSubmitted = selectedAnswer != null
    val isSelected = selectedAnswer == option
    val isCorrect = answerSubmitted && option == correctAnswer

    val containerColor = when {
        isCorrect -> MaterialTheme.colorScheme.secondaryContainer
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }
    val contentColor = when {
        isCorrect -> MaterialTheme.colorScheme.onSecondaryContainer
        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }
    val borderColor = when {
        isCorrect -> MaterialTheme.colorScheme.secondary
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.34f)
    }

    OutlinedButton(
        onClick = onClick,
        enabled = !answerSubmitted,
        modifier = modifier.heightIn(min = 56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor,
            disabledContentColor = contentColor,
        ),
        border = BorderStroke(1.dp, borderColor),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = ('A' + optionIndex).toString(),
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
            )
            Text(
                text = option,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
            )
            if (answerSubmitted) {
                Icon(
                    imageVector = if (isCorrect) Icons.Default.CheckCircle else if (isSelected) Icons.Default.Cancel else Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (isCorrect) MaterialTheme.colorScheme.secondary else if (isSelected) MaterialTheme.colorScheme.error else contentColor.copy(alpha = 0f),
                )
            }
        }
    }
}

@Composable
private fun QuizSummaryContent(
    uiState: QuizUiState,
    onRestartQuiz: () -> Unit,
    onViewStats: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scorePercentage = if (uiState.questions.isEmpty()) 0 else (uiState.score * 100) / uiState.questions.size

    GeoQuestBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator(
                        progress = { scorePercentage / 100f },
                        modifier = Modifier.size(92.dp),
                    )
                    Text(
                        text = "Round complete",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Text(
                        text = "You scored ${uiState.score} out of ${uiState.questions.size} (${scorePercentage}%).",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = "Every answer is stored locally so the progress screen can show growth over time.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Button(
                onClick = onRestartQuiz,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Start another round")
            }

            OutlinedButton(
                onClick = onViewStats,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("View statistics")
            }
        }
    }
}
