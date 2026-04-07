package com.rahul.geoquest.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkScheduler(
    private val context: Context,
) {
    fun scheduleCountryRefresh() {
        val request = PeriodicWorkRequestBuilder<CountryRefreshWorker>(24, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            CountryRefreshWork,
            ExistingPeriodicWorkPolicy.UPDATE,
            request,
        )
    }

    fun scheduleDailyReminder() {
        val request = PeriodicWorkRequestBuilder<StudyReminderWorker>(24, TimeUnit.HOURS).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            ReminderWork,
            ExistingPeriodicWorkPolicy.UPDATE,
            request,
        )
    }

    fun cancelDailyReminder() {
        WorkManager.getInstance(context).cancelUniqueWork(ReminderWork)
    }

    companion object {
        private const val CountryRefreshWork = "geoquest_country_refresh"
        private const val ReminderWork = "geoquest_daily_reminder"
    }
}
