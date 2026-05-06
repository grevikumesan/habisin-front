package com.example.habisin.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "habisin_settings")

enum class AppTheme { LIGHT, DARK, SYSTEM }
enum class AppLanguage { EN, ID }

class SettingsManager(private val context: Context) {

    private object Keys {
        val THEME    = stringPreferencesKey("app_theme")
        val LANGUAGE = stringPreferencesKey("app_language")
    }

    val themeFlow: Flow<AppTheme> = context.settingsDataStore.data.map { prefs ->
        runCatching { AppTheme.valueOf(prefs[Keys.THEME] ?: AppTheme.SYSTEM.name) }
            .getOrDefault(AppTheme.SYSTEM)
    }

    val languageFlow: Flow<AppLanguage> = context.settingsDataStore.data.map { prefs ->
        runCatching { AppLanguage.valueOf(prefs[Keys.LANGUAGE] ?: AppLanguage.EN.name) }
            .getOrDefault(AppLanguage.EN)
    }

    suspend fun setTheme(theme: AppTheme) {
        context.settingsDataStore.edit { it[Keys.THEME] = theme.name }
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.settingsDataStore.edit { it[Keys.LANGUAGE] = language.name }
    }
}