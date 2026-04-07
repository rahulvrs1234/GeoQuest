package com.rahul.geoquest.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rahul.geoquest.MainActivity
import com.rahul.geoquest.R

object NotificationHelper {
    private const val ChannelId = "geoquest_study_reminders"
    private const val ChannelName = "Study reminders"
    private const val NotificationId = 2026

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            ChannelId,
            ChannelName,
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "Optional reminders for daily GeoQuest practice."
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    @SuppressLint("MissingPermission")
    fun showStudyReminder(
        context: Context,
        streakDays: Int,
    ) {
        val contentText =
            if (streakDays > 0) {
                "You're on a $streakDays-day streak. One more round keeps it going."
            } else {
                "A short geography round can refresh memory and pattern recall."
            }

        val openAppIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, ChannelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setContentIntent(openAppIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NotificationId, notification)
    }
}

