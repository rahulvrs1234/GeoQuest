package com.rahul.geoquest.domain.quiz

import com.rahul.geoquest.domain.model.Country
import com.rahul.geoquest.domain.model.Difficulty
import com.rahul.geoquest.domain.model.QuizQuestion
import kotlin.random.Random

class QuizEngine(
    private val random: Random = Random.Default,
) {
    fun buildQuestions(
        countries: List<Country>,
        difficulty: Difficulty,
    ): List<QuizQuestion> {
        val candidates = countries
            .filter { country -> country.capital.isNotBlank() }
            .distinctBy { country -> country.code }

        if (candidates.size < 4) {
            return emptyList()
        }

        return candidates
            .shuffled(random)
            .take(difficulty.questionsPerRound.coerceAtMost(candidates.size))
            .mapIndexed { index, country ->
                val incorrectOptions = candidates
                    .filter { other -> other.code != country.code }
                    .shuffled(random)
                    .take(3)
                    .map { option -> option.name }

                val answerOptions = (incorrectOptions + country.name).shuffled(random)
                val isFlagQuestion = index % 2 == 0

                QuizQuestion(
                    prompt = if (isFlagQuestion) {
                        "Which country uses this flag?"
                    } else {
                        "Which country has the capital city ${country.capital}?"
                    },
                    options = answerOptions,
                    correctAnswer = country.name,
                    imageUrl = country.flagUrl.takeIf { isFlagQuestion && it.isNotBlank() },
                    supportingText = "Region: ${country.region.ifBlank { "Unknown" }}",
                )
            }
    }
}

