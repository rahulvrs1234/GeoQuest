package com.rahul.geoquest.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rahul.geoquest.data.preferences.UserPreferences
import com.rahul.geoquest.data.preferences.UserPreferencesRepository
import com.rahul.geoquest.ui.home.HomeScreen
import com.rahul.geoquest.ui.home.HomeViewModel
import com.rahul.geoquest.ui.navigation.GeoQuestDestination
import com.rahul.geoquest.ui.quiz.QuizScreen
import com.rahul.geoquest.ui.quiz.QuizViewModel
import com.rahul.geoquest.ui.settings.SettingsScreen
import com.rahul.geoquest.ui.settings.SettingsViewModel
import com.rahul.geoquest.ui.stats.StatsScreen
import com.rahul.geoquest.ui.stats.StatsViewModel
import com.rahul.geoquest.ui.theme.GeoQuestTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun GeoQuestApp() {
    val preferencesRepository: UserPreferencesRepository = koinInject()
    val preferences by preferencesRepository.settingsFlow.collectAsStateWithLifecycle(
        initialValue = UserPreferences(),
    )
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    val bottomDestinations = listOf(
        GeoQuestDestination.Home,
        GeoQuestDestination.Stats,
        GeoQuestDestination.Settings,
    )

    fun navigateToTopLevel(route: String) {
        when (route) {
            GeoQuestDestination.Home.route -> {
                if (!navController.popBackStack(GeoQuestDestination.Home.route, inclusive = false)) {
                    navController.navigate(GeoQuestDestination.Home.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }

            else -> {
                navController.navigate(route) {
                    popUpTo(GeoQuestDestination.Home.route) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }

    GeoQuestTheme(themeMode = preferences.themeMode) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (currentDestination?.route != GeoQuestDestination.Quiz.route) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                        windowInsets = NavigationBarDefaults.windowInsets,
                    ) {
                        bottomDestinations.forEach { destination ->
                            NavigationBarItem(
                                selected = currentDestination?.hierarchy?.any { navDestination ->
                                    navDestination.route == destination.route
                                } == true,
                                onClick = {
                                    navigateToTopLevel(destination.route)
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                                icon = {
                                    val icon = when (destination) {
                                        GeoQuestDestination.Home -> Icons.Default.Home
                                        GeoQuestDestination.Stats -> Icons.Default.BarChart
                                        GeoQuestDestination.Settings -> Icons.Default.Settings
                                        GeoQuestDestination.Quiz -> Icons.Default.Home
                                    }
                                    Icon(imageVector = icon, contentDescription = destination.label)
                                },
                                label = { Text(destination.label) },
                            )
                        }
                    }
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = GeoQuestDestination.Home.route,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(GeoQuestDestination.Home.route) {
                    val viewModel: HomeViewModel = koinViewModel()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    HomeScreen(
                        uiState = uiState,
                        onStartQuiz = { navController.navigate(GeoQuestDestination.Quiz.route) },
                        onRefresh = { viewModel.refresh(forceSync = true) },
                        onOpenStats = { navigateToTopLevel(GeoQuestDestination.Stats.route) },
                        onOpenSettings = { navigateToTopLevel(GeoQuestDestination.Settings.route) },
                    )
                }

                composable(GeoQuestDestination.Stats.route) {
                    val viewModel: StatsViewModel = koinViewModel()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    StatsScreen(
                        uiState = uiState,
                        onRefresh = viewModel::refresh,
                    )
                }

                composable(GeoQuestDestination.Settings.route) {
                    val viewModel: SettingsViewModel = koinViewModel()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    SettingsScreen(
                        uiState = uiState,
                        onDifficultySelected = viewModel::setDifficulty,
                        onReminderChanged = viewModel::setDailyRemindersEnabled,
                        onHintsChanged = viewModel::setShowRegionHints,
                        onThemeModeSelected = viewModel::setThemeMode,
                    )
                }

                composable(GeoQuestDestination.Quiz.route) {
                    val viewModel: QuizViewModel = koinViewModel()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    QuizScreen(
                        uiState = uiState,
                        onAnswerSelected = viewModel::selectAnswer,
                        onRevealHint = viewModel::revealHint,
                        onNextQuestion = viewModel::moveToNextQuestion,
                        onRestartQuiz = viewModel::startNewQuiz,
                        onExit = { navController.popBackStack() },
                        onViewStats = {
                            navController.navigate(GeoQuestDestination.Stats.route) {
                                popUpTo(GeoQuestDestination.Home.route) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        },
                    )
                }
            }
        }
    }
}
