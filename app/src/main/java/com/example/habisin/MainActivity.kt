package com.example.habisin

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.habisin.data.local.AppLanguage
import com.example.habisin.data.local.AppTheme
import androidx.lifecycle.lifecycleScope
import com.example.habisin.data.local.SettingsManager
import com.example.habisin.notif.NotificationScheduler
import com.example.habisin.ui.router.AppRouter
import com.example.habisin.ui.theme.HabisInTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale

class MainActivity : ComponentActivity() {

    // Apply the saved language to this Activity's resources BEFORE anything inflates.
    // (A Compose ComponentActivity doesn't reliably pick up AppCompatDelegate locales,
    // so we wrap the base context directly; changing language recreate()s the Activity.)
    override fun attachBaseContext(newBase: Context) {
        val lang = runBlocking { SettingsManager(newBase).languageFlow.first() }
        val tag = when (lang) {
            AppLanguage.EN -> "en"
            AppLanguage.ID -> "id"
        }
        val locale = Locale(tag)
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val settings = SettingsManager(applicationContext)

        // Expiry notifications: create channel + (re)schedule the background poll if enabled.
        // All off the main thread so DataStore/WorkManager never block startup (avoids ANR).
        lifecycleScope.launch {
            NotificationScheduler.ensureChannel(applicationContext)
            if (settings.isNotifEnabled()) {
                NotificationScheduler.schedulePeriodic(applicationContext)
            }
        }

        setContent {
            val themePref by settings.themeFlow.collectAsState(initial = AppTheme.SYSTEM)

            val darkTheme = when (themePref) {
                AppTheme.LIGHT  -> false
                AppTheme.DARK   -> true
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }

            HabisInTheme(darkTheme = darkTheme) {
                AppRouter()
            }
        }
    }
}