package com.app.rrq.data.local

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("rrq_session", Context.MODE_PRIVATE)

    fun saveUser(nama: String, email: String, telepon: String, role: String, uid: String) {
        prefs.edit()
            .putString("nama", nama)
            .putString("email", email)
            .putString("telepon", telepon)
            .putString("role", role)
            .putString("uid", uid)
            .apply()
    }

    fun getNama(): String = prefs.getString("nama", "") ?: ""
    fun getEmail(): String = prefs.getString("email", "") ?: ""
    fun getTelepon(): String = prefs.getString("telepon", "") ?: ""
    fun getRole(): String = prefs.getString("role", "user") ?: "user"
    fun getUid(): String = prefs.getString("uid", "") ?: ""

    fun clear() = prefs.edit().clear().apply()
}