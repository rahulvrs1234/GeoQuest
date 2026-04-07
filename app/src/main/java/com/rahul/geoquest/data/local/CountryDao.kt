package com.rahul.geoquest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CountryDao {
    @Query("SELECT * FROM countries ORDER BY name ASC")
    suspend fun getAll(): List<CountryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(countries: List<CountryEntity>)

    @Query("DELETE FROM countries")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM countries")
    suspend fun count(): Int

    @Query("SELECT MAX(syncedAtMillis) FROM countries")
    suspend fun latestSyncMillis(): Long?
}

