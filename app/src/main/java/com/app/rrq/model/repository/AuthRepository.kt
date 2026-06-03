package com.app.rrq.model.repository

import com.app.rrq.core.utils.HashUtils
import com.app.rrq.core.utils.SystemLogger
import com.app.rrq.model.data.LoginResult
import com.app.rrq.model.data.UserAccount
import com.app.rrq.model.datasource.GistUserDataSource
import kotlinx.coroutines.delay

class AuthRepository {

    // Hashed mock passwords
    // "admin123" -> 240753d49def217b0f2ecd65165832e1d5231c611c02b4fc361dec0e03e7eef2
    // "user123"  -> 0b7074e64f7b60efb7964b732fbdf6b3b5c7f8976b05eb41603598d1d87e0fa0
    private val localMockUsers = listOf(
        UserAccount(
            id = "1",
            name = "Admin Satu",
            email = "admin@roadresq.com",
            password = HashUtils.sha256("admin123"),
            phoneNumber = "081234567890",
            role = "Admin",
            accountStatus = "ACTIVE",
            createdAt = "2026-05-20T00:00:00Z"
        ),
        UserAccount(
            id = "2",
            name = "User Satu",
            email = "user@roadresq.com",
            password = HashUtils.sha256("user123"),
            phoneNumber = "081234567891",
            role = "User",
            accountStatus = "ACTIVE",
            createdAt = "2026-05-20T01:00:00Z"
        )
    )

    suspend fun login(email: String, password: String): LoginResult {
        delay(1000) // Simulasi network delay
        
        // 1. Ambil data dari Gist
        val users = try {
            GistUserDataSource.fetchUsers()
        } catch (e: Exception) {
            // Log fallback ke data lokal
            SystemLogger.logActivity("Gist fetch failed, using local fallback: ${e.message}", email)
            localMockUsers
        }

        // 2. Hash input password
        val inputHashedPassword = HashUtils.sha256(password)

        // 3. Cari user yang cocok
        val matchedUser = users.find { 
            it.email.equals(email, ignoreCase = true) && it.password == inputHashedPassword 
        }

        if (matchedUser != null) {
            if (matchedUser.accountStatus != "ACTIVE") {
                throw Exception("Akun Anda sedang dinonaktifkan")
            }
            SystemLogger.logActivity("${matchedUser.role} Logged In Successfully", email)
            return LoginResult(
                userId = matchedUser.id,
                email  = matchedUser.email,
                name   = matchedUser.name,
                role   = matchedUser.role
            )
        }

        SystemLogger.logActivity("Failed Login Attempt (Invalid Credentials)", email)
        throw Exception("Email atau password salah")
    }

    suspend fun register(name: String, email: String, password: String): Boolean {
        delay(1000)
        if (name.isNotBlank() && email.contains("@") && password.length >= 6) {
            SystemLogger.logActivity("New User Registered", email)
            return true
        }
        throw Exception("Data registrasi tidak valid")
    }
}
