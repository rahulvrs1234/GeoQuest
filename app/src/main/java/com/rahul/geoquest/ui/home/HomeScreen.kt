package com.rahul.geoquest.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rahul.geoquest.ui.components.GeoQuestBackground
import com.rahul.geoquest.ui.components.InfoCard
import com.rahul.geoquest.ui.components.ScrollableScreen

data class HomeUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val difficultyLabel: String = "Medium",
    val streakDays: Int = 0,
    val quizzesCompleted: Int = 0,
    val accuracyPercentage: Int = 0,
    val countryCount: Int = 0,
    val latestSyncLabel: String = "Not synced yet",
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onStartQuiz: () -> Unit,
    onRefresh: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    if (uiState.isLoading) {
        GeoQuestBackground {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator()
                Text(
                    text = "Preparing your next geography round...",
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        }
        return
    }

    ScrollableScreen {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.TravelExplore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "GeoQuest",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Text(
                            text = "Short geography rounds that feel calm, focused, and worth repeating.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.82f),
                        )
                    }
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AssistChip(
                        onClick = {},
                        label = { Text("${uiState.countryCount} countries ready") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                            )
                        },
                    )
                    AssistChip(
                        onClick = {},
                        label = { Text("${uiState.difficultyLabel} mode") },
                    )
                }

                Button(
                    onClick = onStartQuiz,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Start practice round")
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        }

        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            InfoCard(
                title = "Current streak",
                value = "${uiState.streakDays} day${if (uiState.streakDays == 1) "" else "s"}",
                supportingText = "Keep recall active with one short quiz each day.",
                modifier = Modifier.weight(1f),
            )
            InfoCard(
                title = "Accuracy",
                value = "${uiState.accuracyPercentage}%",
                supportingText = "Based on your recorded quiz results.",
                modifier = Modifier.weight(1f),
            )
        }

        InfoCard(
            title = "Library status",
            value = "${uiState.countryCount} countries cached",
            supportingText = "Current difficulty: ${uiState.difficultyLabel}. Last sync: ${uiState.latestSyncLabel}",
        )

        FilledTonalButton(
            onClick = onRefresh,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            Text(
                text = "Sync learning content",
                modifier = Modifier.padding(start = 8.dp),
            )
        }

        OutlinedButton(
            onClick = onOpenStats,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(imageVector = Icons.Default.Leaderboard, contentDescription = null)
            Text(
                text = "View progress",
                modifier = Modifier.padding(start = 8.dp),
            )
        }

        OutlinedButton(
            onClick = onOpenSettings,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = null)
            Text(
                text = "Settings",
                modifier = Modifier.padding(start = 8.dp),
            )
        }

        InfoCard(
            title = "Ethical design note",
            value = "${uiState.quizzesCompleted} quiz sessions stored locally",
            supportingText = "This app keeps only study progress on-device, uses reminders only when you opt in, and avoids dark-pattern nudges.",
        )
    }
}
