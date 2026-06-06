package com.example.habisin.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore by preferencesDataStore(name = "habisin_session")

class SessionManager(private val context: Context) {

    private val store = context.sessionDataStore

    //Keys
    companion object {
        private val KEY_TOKEN      = stringPreferencesKey("token")
        private val KEY_USERNAME   = stringPreferencesKey("username")
        private val KEY_EMAIL      = stringPreferencesKey("email")
    }

    //Write

    suspend fun saveSession(token: String) {
        store.edit { prefs ->
            prefs[KEY_TOKEN]      = token
        }
    }

    /** Simpan identitas user buat ditampilin di Profile (login cuma balikin token). */
    suspend fun saveUserInfo(username: String?, email: String?) {
        store.edit { prefs ->
            username?.let { prefs[KEY_USERNAME] = it }
            email?.let { prefs[KEY_EMAIL] = it }
        }
    }

    suspend fun clearSession() {
        store.edit { it.clear() }
    }

    //Read

    suspend fun getToken(): String? =
        store.data.map { it[KEY_TOKEN] }.first()

    suspend fun getUsername(): String? =
        store.data.map { it[KEY_USERNAME] }.first()

    suspend fun getEmail(): String? =
        store.data.map { it[KEY_EMAIL] }.first()

    suspend fun isLoggedIn(): Boolean =
        getToken() != null

    //Read

    val tokenFlow: Flow<String?> =
        store.data.map { it[KEY_TOKEN] }

    val isLoggedInFlow: Flow<Boolean> =
        tokenFlow.map { it != null }
}