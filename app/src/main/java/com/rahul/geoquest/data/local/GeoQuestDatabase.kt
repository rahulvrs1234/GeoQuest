package com.rahul.geoquest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CountryEntity::class, QuizAttemptEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class GeoQuestDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
    abstract fun quizAttemptDao(): QuizAttemptDao
}

