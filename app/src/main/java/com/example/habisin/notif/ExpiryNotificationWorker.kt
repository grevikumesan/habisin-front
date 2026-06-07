package com.example.habisin.notif

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.habisin.R
import com.example.habisin.data.local.SettingsManager
import com.example.habisin.data.remote.container.AppContainer
import com.example.habisin.data.remote.dto.NotificationItem

/**
 * Polls GET /api/notifications?days=<threshold> and posts a single local notification
 * summarising expired + expiring-soon items. The expiry logic lives on the BE; this worker
 * only fetches that feed and surfaces it on the device.
 */
class ExpiryNotificationWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val settings = SettingsManager(applicationContext)
        if (!settings.isNotifEnabled()) return Result.success()

        val container = AppContainer(applicationContext)

        // Not logged in → nothing to check.
        if (container.sessionManager.getToken().isNullOrEmpty()) return Result.success()

        val threshold = settings.getNotifThreshold()

        val data = try {
            val response = container.notificationService.getNotifications(threshold)
            if (!response.isSuccessful) return Result.retry()
            response.body()?.data ?: return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }

        val expired = data.expired
        val expiring = data.expiringSoon
        val isTest = inputData.getBoolean(NotificationScheduler.KEY_TEST, false)

        if (expired.isEmpty() && expiring.isEmpty()) {
            // A manual test should always confirm it works, even when nothing's expiring.
            if (isTest) postNotification("Notifikasi aktif ✓", "Belum ada bahan yang mendekati kedaluwarsa.")
            return Result.success()
        }

        val (title, body) = buildMessage(expired, expiring)
        postNotification(title, body)
        return Result.success()
    }

    private fun buildMessage(
        expired: List<NotificationItem>,
        expiring: List<NotificationItem>
    ): Pair<String, String> {
        val title = when {
            expired.isNotEmpty() && expiring.isNotEmpty() ->
                "${expired.size} kedaluwarsa, ${expiring.size} segera habis"
            expired.isNotEmpty() -> "${expired.size} bahan sudah kedaluwarsa"
            else -> "${expiring.size} bahan segera kedaluwarsa"
        }

        val parts = mutableListOf<String>()
        expiring.take(3).forEach { item ->
            val d = item.daysLeft
            parts += if (d <= 0) "${item.displayName} (hari ini)"
                     else "${item.displayName} ($d hari lagi)"
        }
        expired.take(2).forEach { parts += "${it.displayName} (lewat)" }
        val body = if (parts.isEmpty()) "Cek kulkasmu di Habisin." else parts.joinToString(", ")
        return title to body
    }

    private fun postNotification(title: String, body: String) {
        // Android 13+: must have runtime permission to post.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        NotificationScheduler.ensureChannel(applicationContext)

        val notification = NotificationCompat.Builder(
            applicationContext,
            NotificationScheduler.channelId(applicationContext)
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)   // heads-up on Android < 8 too
            // On Android 8+ the channel controls the sound; this covers < 8.
            .setSound(NotificationScheduler.resolveSoundUri(applicationContext))
            .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext)
            .notify(NotificationScheduler.NOTIFICATION_ID, notification)
    }
}
