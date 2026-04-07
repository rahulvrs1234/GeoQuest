package com.rahul.geoquest.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.rahul.geoquest.domain.model.AppThemeMode
import com.rahul.geoquest.domain.model.Difficulty
import com.rahul.geoquest.ui.components.GeoQuestBackground
import com.rahul.geoquest.ui.components.ScrollableScreen

data class SettingsUiState(
    val isLoading: Boolean = true,
    val difficulty: Difficulty = Difficulty.Medium,
    val dailyRemindersEnabled: Boolean = false,
    val showRegionHints: Boolean = true,
    val themeMode: AppThemeMode = AppThemeMode.System,
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onDifficultySelected: (Difficulty) -> Unit,
    onReminderChanged: (Boolean) -> Unit,
    onHintsChanged: (Boolean) -> Unit,
    onThemeModeSelected: (AppThemeMode) -> Unit,
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

    val context = LocalContext.current
    var permissionMessage by rememberSaveable { mutableStateOf<String?>(null) }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            onReminderChanged(true)
            permissionMessage = null
        } else {
            permissionMessage = "Notifications stay off unless you explicitly allow them."
            onReminderChanged(false)
        }
    }

    ScrollableScreen {
        Text(
            text = "Study settings",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Tune the look and feel, quiz support, and reminders without hiding how the app behaves.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Appearance",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Choose whether GeoQuest follows the phone theme or uses a fixed light or dark look.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AppThemeMode.entries.forEach { themeMode ->
                        FilterChip(
                            selected = themeMode == uiState.themeMode,
                            onClick = { onThemeModeSelected(themeMode) },
                            label = { Text(themeMode.label) },
                        )
                    }
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Difficulty",
                    style = MaterialTheme.typography.titleMedium,
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Difficulty.entries.forEach { difficulty ->
                        FilterChip(
                            selected = difficulty == uiState.difficulty,
                            onClick = { onDifficultySelected(difficulty) },
                            label = { Text(difficulty.label) },
                        )
                    }
                }
            }
        }

        SettingsToggleRow(
            title = "Show region hints",
            supportingText = "Keeps quizzes supportive for younger learners and reduces frustration while recall is still forming.",
            checked = uiState.showRegionHints,
            onCheckedChange = onHintsChanged,
        )

        SettingsToggleRow(
            title = "Daily study reminders",
            supportingText = "Optional only. Reminders are scheduled with WorkManager and can be switched off anytime.",
            checked = uiState.dailyRemindersEnabled,
            onCheckedChange = { enabled ->
                val notificationPermissionGranted =
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

                if (enabled && !notificationPermissionGranted) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    onReminderChanged(enabled)
                    permissionMessage = null
                }
            },
        )

        permissionMessage?.let { message ->
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = "Privacy and ethics",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "GeoQuest stores quiz history locally for the statistics screen, fetches geography content from a public API, and avoids manipulative streak pressure by keeping reminders optional and transparent.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    supportingText: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
        }
    }
}
