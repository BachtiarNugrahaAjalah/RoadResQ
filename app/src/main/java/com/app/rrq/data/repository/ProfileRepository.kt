package com.app.rrq.data.repository

import android.net.Uri
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ProfileRepository {

    private val auth: FirebaseAuth get() = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore get() = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage get() = FirebaseStorage.getInstance()

    /** Update nama pengguna di Firestore */
    suspend fun updateNama(uid: String, namaBaru: String): Result<Unit> {
        return try {
            firestore.collection("users").document(uid)
                .update("name", namaBaru)
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
     * Upload foto profil ke Firebase Storage dan simpan URL ke Firestore.
     * @param uid UID pengguna
     * @param imageUri URI gambar lokal yang dipilih
     * @return URL download dari Firebase Storage
     */
    suspend fun uploadFotoProfil(uid: String, imageUri: Uri): Result<String> {
        return try {
            val ref = storage.reference.child("profile_photos/$uid.jpg")

            // Upload file
            ref.putFile(imageUri).await()

            // Ambil URL download
            val downloadUrl = ref.downloadUrl.await().toString()

            // Simpan URL ke Firestore
            firestore.collection("users").document(uid)
                .update("photoUrl", downloadUrl)
                .await()

            Result.success(downloadUrl)
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
