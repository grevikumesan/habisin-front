package com.example.habisin.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// Extension property — creates a single DataStore instance tied to the app context.
// Must be at top level (outside the class), declared once across the whole app.
// TODO (teammates): if another feature also needs its own DataStore, create a
// separate val with a different name here, e.g. val Context.userDataStore by preferencesDataStore("user_prefs")
private val Context.sessionDataStore by preferencesDataStore(name = "habisin_session")

/**
 * SessionManager — stores JWT token and basic user info using Jetpack DataStore.
 *
 * All reads return a Flow (reactive). For one-shot reads inside a suspend function,
 * call .first() on the flow (already done in getToken() / getUser() below).
 *
 * TODO (teammates): if your feature needs the logged-in user's ID or name,
 * inject SessionManager through AuthContainer and call getUser() in a coroutine.
 */
class SessionManager(private val context: Context) {

    private val store = context.sessionDataStore

    // ── Keys ───────────────────────────────────────────────────────────────

    companion object {
        private val KEY_TOKEN      = stringPreferencesKey("token")
    }

    // ── Write ──────────────────────────────────────────────────────────────

    suspend fun saveSession(token: String) {
        store.edit { prefs ->
            prefs[KEY_TOKEN]      = token
        }
    }

    suspend fun clearSession() {
        store.edit { it.clear() }
    }

    // ── Read (one-shot, for use inside suspend functions) ──────────────────

    suspend fun getToken(): String? =
        store.data.map { it[KEY_TOKEN] }.first()

    suspend fun isLoggedIn(): Boolean =
        getToken() != null

    // ── Read (reactive Flow, for observing in a ViewModel) ─────────────────

    val tokenFlow: Flow<String?> =
        store.data.map { it[KEY_TOKEN] }

    val isLoggedInFlow: Flow<Boolean> =
        tokenFlow.map { it != null }
}