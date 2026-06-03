package com.app.rrq.core.session

import android.content.Context
import android.content.SharedPreferences

object SessionManager {

    private const val PREF_NAME  = "rrq_session"
    private const val KEY_LOGGED  = "is_logged_in"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_ROLE    = "user_role"
    private const val KEY_NAME    = "user_name"
    private const val KEY_EMAIL   = "user_email"

    private lateinit var prefs: SharedPreferences

    /** Dipanggil sekali dari RoadResQApplication.onCreate() */
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // ── Getters / Setters ────────────────────────────────────────────────
    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_LOGGED, false)
        private set(value) = prefs.edit().putBoolean(KEY_LOGGED, value).apply()

    var userId: String
        get() = prefs.getString(KEY_USER_ID, "") ?: ""
        private set(value) = prefs.edit().putString(KEY_USER_ID, value).apply()

    var userRole: String
        get() = prefs.getString(KEY_ROLE, "User") ?: "User"
        private set(value) = prefs.edit().putString(KEY_ROLE, value).apply()

    var userName: String
        get() = prefs.getString(KEY_NAME, "") ?: ""
        private set(value) = prefs.edit().putString(KEY_NAME, value).apply()

    var userEmail: String
        get() = prefs.getString(KEY_EMAIL, "") ?: ""
        private set(value) = prefs.edit().putString(KEY_EMAIL, value).apply()

    val isAdmin: Boolean get() = userRole == "Admin"

    // ── Session Actions ──────────────────────────────────────────────────
    fun saveSession(userId: String, email: String, name: String, role: String) {
        isLoggedIn = true
        this.userId   = userId
        this.userEmail = email
        this.userName  = name
        this.userRole  = role
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
