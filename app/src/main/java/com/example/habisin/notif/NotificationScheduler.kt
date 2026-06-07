package com.example.habisin.notif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.habisin.data.local.SettingsManager
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

/** Owns the notification channel + (un)scheduling of the periodic expiry check. */
object NotificationScheduler {

    const val NOTIFICATION_ID = 1001
    const val KEY_TEST = "is_test"
    private const val CHANNEL_PREFIX = "habisin_expiry_"
    private const val PERIODIC_WORK = "habisin_expiry_periodic"
    private const val ONE_TIME_WORK = "habisin_expiry_once"

    /** Resolve the sound: user's pick → bundled Habisin sound (res/raw/habisin_notif) → system default. */
    fun resolveSoundUri(context: Context): Uri {
        val userUri = runBlocking { SettingsManager(context).getNotifSoundUri() }
        if (!userUri.isNullOrBlank()) return Uri.parse(userUri)

        val bundled = context.resources.getIdentifier("habisin_notif", "raw", context.packageName)
        if (bundled != 0) return Uri.parse("android.resource://${context.packageName}/$bundled")

        return Settings.System.DEFAULT_NOTIFICATION_URI
    }

    /** A channel's sound is immutable after creation, so we key the channel id to the chosen sound. */
    fun channelId(context: Context): String {
        val userUri = runBlocking { SettingsManager(context).getNotifSoundUri() }
        val key = when {
            !userUri.isNullOrBlank() -> Integer.toHexString(userUri.hashCode())
            context.resources.getIdentifier("habisin_notif", "raw", context.packageName) != 0 -> "habisin"
            else -> "default"
        }
        return CHANNEL_PREFIX + key
    }

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NotificationManager::class.java)
            val id = channelId(context)
            if (manager.getNotificationChannel(id) == null) {
                val channel = NotificationChannel(
                    id,
                    "Pengingat Kedaluwarsa",
                    NotificationManager.IMPORTANCE_HIGH   // heads-up banner + sound + vibration
                ).apply {
                    description = "Pemberitahuan bahan yang akan/sudah kedaluwarsa"
                    enableVibration(true)
                    val attrs = AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                    setSound(resolveSoundUri(context), attrs)
                }
                manager.createNotificationChannel(channel)
            }
        }
    }

    /** Call after the user changes the sound: drop the old Habisin channels, recreate with the new one. */
    fun applySoundChange(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.notificationChannels
                .filter { it.id.startsWith(CHANNEL_PREFIX) }
                .forEach { manager.deleteNotificationChannel(it.id) }
        }
        ensureChannel(context)
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
        val request = OneTimeWorkRequestBuilder<ExpiryNotificationWorker>()
            .setInputData(workDataOf(KEY_TEST to true))
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            ONE_TIME_WORK,
            androidx.work.ExistingWorkPolicy.REPLACE,
            request
        )
    }
}
