package com.app.rrq.data.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class ProfileRepository {

    private val auth: FirebaseAuth get() = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore get() = FirebaseFirestore.getInstance()

    /** Update nama pengguna di Firestore */
    suspend fun updateNama(uid: String, namaBaru: String): Result<Unit> {
        return try {
            // Gunakan set+merge agar tidak gagal meski field belum ada
            firestore.collection("users").document(uid)
                .set(mapOf("name" to namaBaru), SetOptions.merge())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update email: re-autentikasi dengan password, lalu update email
     * di Firebase Auth dan Firestore.
     */
    suspend fun updateEmail(
        emailLama: String,
        password: String,
        emailBaru: String,
        uid: String
    ): Result<Unit> {
        return try {
            val user = auth.currentUser
                ?: return Result.failure(Exception("Pengguna tidak ditemukan"))

            // Re-autentikasi
            val credential = EmailAuthProvider.getCredential(emailLama, password)
            user.reauthenticate(credential).await()

            // Update email di Firebase Auth
            user.verifyBeforeUpdateEmail(emailBaru).await()

            // Update email di Firestore
            firestore.collection("users").document(uid)
                .update("email", emailBaru)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update password: re-autentikasi dengan password lama, lalu update password baru.
     */
    suspend fun updatePassword(
        email: String,
        passwordLama: String,
        passwordBaru: String
    ): Result<Unit> {
        return try {
            val user = auth.currentUser
                ?: return Result.failure(Exception("Pengguna tidak ditemukan"))

            // Re-autentikasi
            val credential = EmailAuthProvider.getCredential(email, passwordLama)
            user.reauthenticate(credential).await()

            // Update password
            user.updatePassword(passwordBaru).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Simpan foto profil sebagai string base64 ke Firestore.
     * Tidak memerlukan Firebase Storage — konsisten dengan pendekatan gambar laporan.
     */
    suspend fun simpanFotoBase64(uid: String, base64: String): Result<String> {
        return try {
            firestore.collection("users").document(uid)
                .set(mapOf("photoBase64" to base64), SetOptions.merge())
                .await()
            Result.success(base64)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Ambil data profil terkini dari Firestore */
    suspend fun getProfile(uid: String): Result<Map<String, Any?>> {
        return try {
            val doc = firestore.collection("users").document(uid).get().await()
            if (doc.exists()) {
                Result.success(doc.data ?: emptyMap())
            } else {
                Result.failure(Exception("Data profil tidak ditemukan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
