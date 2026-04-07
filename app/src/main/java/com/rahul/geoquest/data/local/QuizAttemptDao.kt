package com.rahul.geoquest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuizAttemptDao {
    @Insert
    suspend fun insert(attempt: QuizAttemptEntity)

    @Query("SELECT * FROM quiz_attempts ORDER BY timestampMillis DESC")
    suspend fun getAll(): List<QuizAttemptEntity>
}

