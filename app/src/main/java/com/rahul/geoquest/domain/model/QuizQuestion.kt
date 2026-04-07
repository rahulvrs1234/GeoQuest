package com.rahul.geoquest.domain.model

data class QuizQuestion(
    val prompt: String,
    val options: List<String>,
    val correctAnswer: String,
    val imageUrl: String? = null,
    val supportingText: String? = null,
)

