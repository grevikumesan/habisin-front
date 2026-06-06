package com.example.habisin.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "habisin_settings")

enum class AppTheme { LIGHT, DARK, SYSTEM }
enum class AppLanguage { EN, ID }

class SettingsManager(private val context: Context) {

    private object Keys {
        val THEME            = stringPreferencesKey("app_theme")
        val LANGUAGE         = stringPreferencesKey("app_language")
        val PROFILE_PIC      = stringPreferencesKey("profile_picture_uri")
        val NOTIF_ENABLED    = booleanPreferencesKey("notif_enabled")
        val NOTIF_THRESHOLD  = intPreferencesKey("notif_threshold_days")
    }

    companion object {
        const val DEFAULT_THRESHOLD_DAYS = 3
        const val MIN_THRESHOLD_DAYS = 1
        const val MAX_THRESHOLD_DAYS = 14
    }

    val themeFlow: Flow<AppTheme> = context.settingsDataStore.data.map { prefs ->
        runCatching { AppTheme.valueOf(prefs[Keys.THEME] ?: AppTheme.SYSTEM.name) }
            .getOrDefault(AppTheme.SYSTEM)
    }

    val languageFlow: Flow<AppLanguage> = context.settingsDataStore.data.map { prefs ->
        runCatching { AppLanguage.valueOf(prefs[Keys.LANGUAGE] ?: AppLanguage.EN.name) }
            .getOrDefault(AppLanguage.EN)
    }

    val profilePictureFlow: Flow<String?> = context.settingsDataStore.data.map { prefs ->
        prefs[Keys.PROFILE_PIC]
    }

    // ── Notifications ──
    val notifEnabledFlow: Flow<Boolean> = context.settingsDataStore.data.map { prefs ->
        prefs[Keys.NOTIF_ENABLED] ?: true   // on by default — it's the headline feature
    }

    val notifThresholdFlow: Flow<Int> = context.settingsDataStore.data.map { prefs ->
        prefs[Keys.NOTIF_THRESHOLD] ?: DEFAULT_THRESHOLD_DAYS
    }

    suspend fun isNotifEnabled(): Boolean = notifEnabledFlow.first()
    suspend fun getNotifThreshold(): Int = notifThresholdFlow.first()

    suspend fun setNotifEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { it[Keys.NOTIF_ENABLED] = enabled }
    }

    suspend fun setNotifThreshold(days: Int) {
        context.settingsDataStore.edit {
            it[Keys.NOTIF_THRESHOLD] = days.coerceIn(MIN_THRESHOLD_DAYS, MAX_THRESHOLD_DAYS)
        }
    }

    suspend fun setTheme(theme: AppTheme) {
        context.settingsDataStore.edit { it[Keys.THEME] = theme.name }
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.settingsDataStore.edit { it[Keys.LANGUAGE] = language.name }
    }

    suspend fun setProfilePicture(uri: String) {
        context.settingsDataStore.edit { it[Keys.PROFILE_PIC] = uri }
    }

    suspend fun clearProfilePicture() {
        context.settingsDataStore.edit { it.remove(Keys.PROFILE_PIC) }
    }
}