package com.example.habisin.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habisin.data.local.AppLanguage
import com.example.habisin.data.local.AppTheme
import com.example.habisin.data.local.SettingsManager
import com.example.habisin.notif.NotificationScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val settings = SettingsManager(app)

    val theme: StateFlow<AppTheme> = settings.themeFlow.stateIn(
        scope        = viewModelScope,
        started      = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppTheme.SYSTEM
    )

    val language: StateFlow<AppLanguage> = settings.languageFlow.stateIn(
        scope        = viewModelScope,
        started      = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppLanguage.EN
    )

    val notifEnabled: StateFlow<Boolean> = settings.notifEnabledFlow.stateIn(
        scope        = viewModelScope,
        started      = SharingStarted.WhileSubscribed(5_000),
        initialValue = true
    )

    val notifThreshold: StateFlow<Int> = settings.notifThresholdFlow.stateIn(
        scope        = viewModelScope,
        started      = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsManager.DEFAULT_THRESHOLD_DAYS
    )

    val notifSoundName: StateFlow<String?> = settings.notifSoundNameFlow.stateIn(
        scope        = viewModelScope,
        started      = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch { settings.setTheme(theme) }
    }

    fun setNotifEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settings.setNotifEnabled(enabled)
            val ctx = getApplication<Application>()
            if (enabled) NotificationScheduler.schedulePeriodic(ctx)
            else NotificationScheduler.cancel(ctx)
        }
    }

    fun setNotifThreshold(days: Int) {
        viewModelScope.launch {
            settings.setNotifThreshold(days)
            // reschedule so the new threshold takes effect
            if (settings.isNotifEnabled()) {
                NotificationScheduler.schedulePeriodic(getApplication())
            }
        }
    }

    /** Set a custom notification sound (null = Habisin/default). Rebuilds the channel. */
    fun setNotifSound(uri: String?, name: String?) {
        viewModelScope.launch {
            settings.setNotifSound(uri, name)
            NotificationScheduler.applySoundChange(getApplication())
        }
    }

    /** "Test notification" — runs one check immediately. */
    fun sendTestNotification() {
        NotificationScheduler.runOnce(getApplication())
    }

    fun setLanguage(lang: AppLanguage, onApplied: () -> Unit = {}) {
        viewModelScope.launch {
            settings.setLanguage(lang)   // persist first, then recreate so the new locale loads
            onApplied()
        }
    }
}