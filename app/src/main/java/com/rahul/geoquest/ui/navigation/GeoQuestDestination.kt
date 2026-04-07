package com.rahul.geoquest.ui.navigation

sealed class GeoQuestDestination(
    val route: String,
    val label: String,
) {
    data object Home : GeoQuestDestination("home", "Home")
    data object Stats : GeoQuestDestination("stats", "Stats")
    data object Settings : GeoQuestDestination("settings", "Settings")
    data object Quiz : GeoQuestDestination("quiz", "Quiz")
}

