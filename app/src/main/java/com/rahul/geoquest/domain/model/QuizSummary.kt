package com.rahul.geoquest.domain.model

data class QuizSummary(
    val score: Int,
    val totalQuestions: Int,
    val difficulty: String,
    val timestampMillis: Long,
    val durationSeconds: Long,
)

