package com.rahul.geoquest.domain.model

enum class AppThemeMode(
    val label: String,
) {
    System("System"),
    Light("Light"),
    Dark("Dark"),
    ;

    companion object {
        fun fromValue(value: String): AppThemeMode =
            entries.firstOrNull { mode -> mode.name == value } ?: System
    }
}
