package com.rahul.geoquest.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rahul.geoquest.data.repository.GeoQuestRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CountryRefreshWorker(
    appContext: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(appContext, workerParameters), KoinComponent {
    private val repository: GeoQuestRepository by inject()

    override suspend fun doWork(): Result =
        repository.syncCountries()
            .fold(
                onSuccess = { Result.success() },
                onFailure = { Result.retry() },
            )
}

