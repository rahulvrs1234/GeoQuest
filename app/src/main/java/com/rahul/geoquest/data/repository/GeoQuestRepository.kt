package com.rahul.geoquest.data.repository

import androidx.room.withTransaction
import com.rahul.geoquest.data.local.CountryDao
import com.rahul.geoquest.data.local.CountryEntity
import com.rahul.geoquest.data.local.GeoQuestDatabase
import com.rahul.geoquest.data.local.QuizAttemptDao
import com.rahul.geoquest.data.local.QuizAttemptEntity
import com.rahul.geoquest.data.remote.RestCountriesApi
import com.rahul.geoquest.data.remote.toEntityOrNull
import com.rahul.geoquest.domain.model.Country
import com.rahul.geoquest.domain.model.Difficulty
import com.rahul.geoquest.domain.model.QuizQuestion
import com.rahul.geoquest.domain.model.QuizSummary
import com.rahul.geoquest.domain.model.UserStats
import com.rahul.geoquest.domain.quiz.QuizEngine
import com.rahul.geoquest.domain.quiz.UserStatsCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class DashboardSummary(
    val stats: UserStats,
    val countryCount: Int,
    val latestSyncMillis: Long?,
)

data class StatsSummary(
    val stats: UserStats,
    val recentQuizzes: List<QuizSummary>,
)

class GeoQuestRepository(
    private val api: RestCountriesApi,
    private val database: GeoQuestDatabase,
    private val quizAttemptDao: QuizAttemptDao,
    private val quizEngine: QuizEngine,
) {
    private val countryDao: CountryDao = database.countryDao()

    suspend fun syncCountries(): Result<Int> =
        withContext(Dispatchers.IO) {
            runCatching {
                val syncedAtMillis = System.currentTimeMillis()
                val remoteCountries = api.getCountries()
                    .mapNotNull { dto -> dto.toEntityOrNull(syncedAtMillis) }
                    .filter { entity -> entity.capital.isNotBlank() }
                    .distinctBy { entity -> entity.code }

                val countriesToPersist = if (remoteCountries.isEmpty()) fallbackCountries(syncedAtMillis) else remoteCountries

                database.withTransaction {
                    countryDao.clearAll()
                    countryDao.upsertAll(countriesToPersist)
                }

                countriesToPersist.size
            }.recoverCatching {
                ensureFallbackData()
            }
        }

    suspend fun getDashboardSummary(): DashboardSummary =
        withContext(Dispatchers.IO) {
            ensureCountryData()
            DashboardSummary(
                stats = getStatsInternal(),
                countryCount = countryDao.count(),
                latestSyncMillis = countryDao.latestSyncMillis(),
            )
        }

    suspend fun getQuizQuestions(difficulty: Difficulty): List<QuizQuestion> =
        withContext(Dispatchers.IO) {
            ensureCountryData()
            quizEngine.buildQuestions(
                countries = countryDao.getAll().map { entity -> entity.toDomain() },
                difficulty = difficulty,
            )
        }

    suspend fun recordQuizAttempt(
        score: Int,
        totalQuestions: Int,
        difficulty: Difficulty,
        durationSeconds: Long,
    ) {
        withContext(Dispatchers.IO) {
            quizAttemptDao.insert(
                QuizAttemptEntity(
                    score = score,
                    totalQuestions = totalQuestions,
                    difficulty = difficulty.label,
                    timestampMillis = System.currentTimeMillis(),
                    durationSeconds = durationSeconds,
                ),
            )
        }
    }

    suspend fun getStatsSummary(): StatsSummary =
        withContext(Dispatchers.IO) {
            val attempts = quizAttemptDao.getAll()
            StatsSummary(
                stats = UserStatsCalculator.calculate(attempts.map { attempt -> attempt.toSummary() }),
                recentQuizzes = attempts.take(5).map { attempt -> attempt.toSummary() },
            )
        }

    private suspend fun getStatsInternal(): UserStats {
        val attempts = quizAttemptDao.getAll().map { attempt -> attempt.toSummary() }
        return UserStatsCalculator.calculate(attempts)
    }

    private suspend fun ensureCountryData() {
        if (countryDao.count() > 0) {
            return
        }

        syncCountries()
    }

    private suspend fun ensureFallbackData(): Int {
        if (countryDao.count() > 0) {
            return countryDao.count()
        }

        val fallback = fallbackCountries(System.currentTimeMillis())
        database.withTransaction {
            countryDao.clearAll()
            countryDao.upsertAll(fallback)
        }
        return fallback.size
    }

    private fun fallbackCountries(syncedAtMillis: Long): List<CountryEntity> =
        listOf(
            CountryEntity("AU", "Australia", "Canberra", "Oceania", "Australia and New Zealand", "https://flagcdn.com/w320/au.png", 26000000L, syncedAtMillis),
            CountryEntity("JP", "Japan", "Tokyo", "Asia", "Eastern Asia", "https://flagcdn.com/w320/jp.png", 124000000L, syncedAtMillis),
            CountryEntity("BR", "Brazil", "Brasilia", "Americas", "South America", "https://flagcdn.com/w320/br.png", 203000000L, syncedAtMillis),
            CountryEntity("CA", "Canada", "Ottawa", "Americas", "North America", "https://flagcdn.com/w320/ca.png", 41000000L, syncedAtMillis),
            CountryEntity("FR", "France", "Paris", "Europe", "Western Europe", "https://flagcdn.com/w320/fr.png", 68000000L, syncedAtMillis),
            CountryEntity("ZA", "South Africa", "Pretoria", "Africa", "Southern Africa", "https://flagcdn.com/w320/za.png", 62000000L, syncedAtMillis),
            CountryEntity("EG", "Egypt", "Cairo", "Africa", "Northern Africa", "https://flagcdn.com/w320/eg.png", 113000000L, syncedAtMillis),
            CountryEntity("DE", "Germany", "Berlin", "Europe", "Western Europe", "https://flagcdn.com/w320/de.png", 84000000L, syncedAtMillis),
            CountryEntity("IN", "India", "New Delhi", "Asia", "Southern Asia", "https://flagcdn.com/w320/in.png", 1428000000L, syncedAtMillis),
            CountryEntity("MX", "Mexico", "Mexico City", "Americas", "North America", "https://flagcdn.com/w320/mx.png", 129000000L, syncedAtMillis),
            CountryEntity("KE", "Kenya", "Nairobi", "Africa", "Eastern Africa", "https://flagcdn.com/w320/ke.png", 55000000L, syncedAtMillis),
            CountryEntity("NO", "Norway", "Oslo", "Europe", "Northern Europe", "https://flagcdn.com/w320/no.png", 5500000L, syncedAtMillis),
        )
}

private fun CountryEntity.toDomain(): Country =
    Country(
        code = code,
        name = name,
        capital = capital,
        region = region,
        subregion = subregion,
        flagUrl = flagUrl,
        population = population,
    )

private fun QuizAttemptEntity.toSummary(): QuizSummary =
    QuizSummary(
        score = score,
        totalQuestions = totalQuestions,
        difficulty = difficulty,
        timestampMillis = timestampMillis,
        durationSeconds = durationSeconds,
    )
