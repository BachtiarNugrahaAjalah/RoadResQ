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

    // Update individual fields
    fun saveNama(nama: String) = prefs.edit().putString("nama", nama).apply()
    fun saveEmail(email: String) = prefs.edit().putString("email", email).apply()
    fun savePhotoUrl(url: String) = prefs.edit().putString("photo_url", url).apply()
    fun getPhotoUrl(): String = prefs.getString("photo_url", "") ?: ""

    fun clear() = prefs.edit().clear().apply()
}