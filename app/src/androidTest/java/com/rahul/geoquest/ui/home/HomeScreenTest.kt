package com.rahul.geoquest.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreenDisplaysPrimaryActions() {
        composeTestRule.setContent {
            HomeScreen(
                uiState = HomeUiState(
                    isLoading = false,
                    difficultyLabel = "Medium",
                    streakDays = 3,
                    quizzesCompleted = 4,
                    accuracyPercentage = 82,
                    countryCount = 120,
                    latestSyncLabel = "07 Apr 2026, 2:30 PM",
                ),
                onStartQuiz = {},
                onRefresh = {},
                onOpenStats = {},
                onOpenSettings = {},
            )
        }

        composeTestRule.onNodeWithText("GeoQuest").assertIsDisplayed()
        composeTestRule.onNodeWithText("Start practice round").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sync learning content").assertIsDisplayed()
    }
}

