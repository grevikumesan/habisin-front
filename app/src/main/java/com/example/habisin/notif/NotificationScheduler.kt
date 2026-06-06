package com.example.habisin.notif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/** Owns the notification channel + (un)scheduling of the periodic expiry check. */
object NotificationScheduler {

    const val CHANNEL_ID = "habisin_expiry"
    const val NOTIFICATION_ID = 1001
    private const val PERIODIC_WORK = "habisin_expiry_periodic"
    private const val ONE_TIME_WORK = "habisin_expiry_once"

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NotificationManager::class.java)
            if (manager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Pengingat Kedaluwarsa",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Pemberitahuan bahan yang akan/sudah kedaluwarsa"
                }
                manager.createNotificationChannel(channel)
            }
        }
    }

    /** Periodic background poll. 6h interval keeps it timely without draining battery. */
    fun schedulePeriodic(context: Context) {
        ensureChannel(context)
        val request = PeriodicWorkRequestBuilder<ExpiryNotificationWorker>(6, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            PERIODIC_WORK,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(PERIODIC_WORK)
    }

    /** Fire one check right now — used by the "Test notification" button. */
    fun runOnce(context: Context) {
        ensureChannel(context)
        val request = OneTimeWorkRequestBuilder<ExpiryNotificationWorker>().build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            ONE_TIME_WORK,
            androidx.work.ExistingWorkPolicy.REPLACE,
            request
        )
    }
}
