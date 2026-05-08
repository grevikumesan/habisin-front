package com.example.habisin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.os.LocaleListCompat
import com.example.habisin.data.local.AppLanguage
import com.example.habisin.data.local.AppTheme
import com.example.habisin.data.local.SettingsManager
import com.example.habisin.ui.router.AppRouter
import com.example.habisin.ui.theme.HabisInTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val settings = SettingsManager(applicationContext)
        val savedLang = runBlocking { settings.languageFlow.first() }
        val tag = when (savedLang) {
            AppLanguage.EN -> "en"
            AppLanguage.ID -> "id"
        }
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(tag)
        )

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