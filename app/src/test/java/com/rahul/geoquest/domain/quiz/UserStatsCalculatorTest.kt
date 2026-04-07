package com.rahul.geoquest.domain.quiz

import com.rahul.geoquest.domain.model.QuizSummary
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class UserStatsCalculatorTest {
    @Test
    fun `calculate aggregates totals and keeps a current streak`() {
        val zoneId = ZoneId.systemDefault()
        val attempts = listOf(
            QuizSummary(
                score = 5,
                totalQuestions = 5,
                difficulty = "Easy",
                timestampMillis = LocalDate.now().atStartOfDay(zoneId).toInstant().toEpochMilli(),
                durationSeconds = 30,
            ),
            QuizSummary(
                score = 4,
                totalQuestions = 5,
                difficulty = "Medium",
                timestampMillis = LocalDate.now().minusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli(),
                durationSeconds = 45,
            ),
        )

        val stats = UserStatsCalculator.calculate(attempts)

        assertEquals(2, stats.totalQuizzes)
        assertEquals(10, stats.totalQuestions)
        assertEquals(9, stats.correctAnswers)
        assertEquals(90, stats.accuracyPercentage)
        assertEquals(5, stats.bestScore)
        assertEquals(2, stats.currentStreakDays)
    }
}

