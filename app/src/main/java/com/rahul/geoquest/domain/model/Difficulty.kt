package com.rahul.geoquest.domain.model

enum class Difficulty(
    val label: String,
    val questionsPerRound: Int,
) {
    Easy(label = "Easy", questionsPerRound = 5),
    Medium(label = "Medium", questionsPerRound = 7),
    Hard(label = "Hard", questionsPerRound = 10),
    ;

    companion object {
        fun fromValue(value: String): Difficulty =
            entries.firstOrNull { difficulty -> difficulty.name.equals(value, ignoreCase = true) } ?: Medium
    }
}

