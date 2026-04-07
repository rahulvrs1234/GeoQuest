package com.rahul.geoquest.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rahul.geoquest.data.repository.GeoQuestRepository
import com.rahul.geoquest.util.NotificationHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StudyReminderWorker(
    appContext: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(appContext, workerParameters), KoinComponent {
    private val repository: GeoQuestRepository by inject()

    override suspend fun doWork(): Result {
        val hasNotificationPermission =
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED

        if (!hasNotificationPermission) {
            return Result.success()
        }

        val streakDays = repository.getDashboardSummary().stats.currentStreakDays
        NotificationHelper.showStudyReminder(applicationContext, streakDays)
        return Result.success()
    }
}

