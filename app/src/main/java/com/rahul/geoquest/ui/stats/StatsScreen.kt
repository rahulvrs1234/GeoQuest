package com.rahul.geoquest.ui.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rahul.geoquest.domain.model.QuizSummary
import com.rahul.geoquest.domain.model.UserStats
import com.rahul.geoquest.ui.components.GeoQuestBackground
import com.rahul.geoquest.ui.components.InfoCard
import com.rahul.geoquest.ui.components.ScrollableScreen
import com.rahul.geoquest.util.toDisplayDateTime

data class StatsUiState(
    val isLoading: Boolean = true,
    val stats: UserStats = UserStats(),
    val recentQuizzes: List<QuizSummary> = emptyList(),
    val errorMessage: String? = null,
)

@Composable
fun StatsScreen(
    uiState: StatsUiState,
    onRefresh: () -> Unit,
) {
    if (uiState.isLoading) {
        GeoQuestBackground {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        return
    }

    ScrollableScreen {
        Text(
            text = "Progress overview",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "A calm view of practice history, accuracy, and streaks without punishing breaks in study habits.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
            )
        }

        InfoCard(
            title = "Quizzes completed",
            value = uiState.stats.totalQuizzes.toString(),
            supportingText = "${uiState.stats.correctAnswers} correct answers across ${uiState.stats.totalQuestions} questions.",
        )

        InfoCard(
            title = "Accuracy",
            value = "${uiState.stats.accuracyPercentage}%",
            supportingText = "Best single-round score: ${uiState.stats.bestScore}. Current streak: ${uiState.stats.currentStreakDays} day(s).",
        )

        FilledTonalButton(
            onClick = onRefresh,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            Text(
                text = "Refresh statistics",
                modifier = Modifier.padding(start = 8.dp),
            )
        }

        Text(
            text = "Recent sessions",
            style = MaterialTheme.typography.titleLarge,
        )

        if (uiState.recentQuizzes.isEmpty()) {
            Text(
                text = "Your completed quiz rounds will appear here.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            uiState.recentQuizzes.forEach { attempt ->
                RecentSessionCard(attempt = attempt)
            }
        }
    }
}

@Composable
private fun RecentSessionCard(attempt: QuizSummary) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${attempt.score}/${attempt.totalQuestions}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = attempt.difficulty,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = attempt.timestampMillis.toDisplayDateTime(),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "Round length: ${attempt.durationSeconds} seconds.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
