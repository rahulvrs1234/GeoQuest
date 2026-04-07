package com.rahul.geoquest.domain.model

data class UserStats(
    val totalQuizzes: Int = 0,
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val accuracyPercentage: Int = 0,
    val bestScore: Int = 0,
    val currentStreakDays: Int = 0,
)

