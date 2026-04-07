package com.rahul.geoquest.domain.quiz

import com.rahul.geoquest.domain.model.Country
import com.rahul.geoquest.domain.model.Difficulty
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class QuizEngineTest {
    private val countries = listOf(
        Country("AU", "Australia", "Canberra", "Oceania", "Australia and New Zealand", "", 26000000L),
        Country("JP", "Japan", "Tokyo", "Asia", "Eastern Asia", "", 124000000L),
        Country("BR", "Brazil", "Brasilia", "Americas", "South America", "", 203000000L),
        Country("CA", "Canada", "Ottawa", "Americas", "North America", "", 41000000L),
        Country("FR", "France", "Paris", "Europe", "Western Europe", "", 68000000L),
        Country("KE", "Kenya", "Nairobi", "Africa", "Eastern Africa", "", 55000000L),
    )

    @Test
    fun `buildQuestions returns the expected round size for easy difficulty`() {
        val engine = QuizEngine(random = Random(42))

        val questions = engine.buildQuestions(countries, Difficulty.Easy)

        assertEquals(5, questions.size)
    }

    @Test
    fun `each generated question includes the correct answer in its options`() {
        val engine = QuizEngine(random = Random(42))

        val questions = engine.buildQuestions(countries, Difficulty.Medium)

        assertTrue(questions.all { question -> question.correctAnswer in question.options })
        assertTrue(questions.all { question -> question.options.size == 4 })
    }
}

