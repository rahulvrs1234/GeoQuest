package com.rahul.geoquest.domain.quiz

import com.rahul.geoquest.domain.model.QuizSummary
import com.rahul.geoquest.domain.model.UserStats
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object UserStatsCalculator {
    fun calculate(attempts: List<QuizSummary>): UserStats {
        if (attempts.isEmpty()) {
            return UserStats()
        }

        val totalQuestions = attempts.sumOf { attempt -> attempt.totalQuestions }
        val correctAnswers = attempts.sumOf { attempt -> attempt.score }
        val accuracy = if (totalQuestions == 0) 0 else ((correctAnswers.toDouble() / totalQuestions) * 100).toInt()
        val bestScore = attempts.maxOf { attempt -> attempt.score }

        return UserStats(
            totalQuizzes = attempts.size,
            totalQuestions = totalQuestions,
            correctAnswers = correctAnswers,
            accuracyPercentage = accuracy,
            bestScore = bestScore,
            currentStreakDays = currentStreak(attempts),
        )
    }

    private fun currentStreak(attempts: List<QuizSummary>): Int {
        val uniqueDates = attempts
            .map { attempt ->
                Instant.ofEpochMilli(attempt.timestampMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }
            .distinct()
            .sortedDescending()

        if (uniqueDates.isEmpty()) {
            return 0
        }

        var streak = 1
        var expectedDate = uniqueDates.first()

        for (index in 1 until uniqueDates.size) {
            val nextDate = uniqueDates[index]
            if (nextDate == expectedDate.minusDays(1)) {
                streak += 1
                expectedDate = nextDate
            } else {
                break
            }
        }

        val today = LocalDate.now()
        val latestDate = uniqueDates.first()
        return if (latestDate == today || latestDate == today.minusDays(1)) streak else 0
    }
}

