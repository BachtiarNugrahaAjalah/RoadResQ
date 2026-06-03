package com.app.rrq.model.repository

import com.app.rrq.core.utils.SystemLogger
import com.app.rrq.model.data.Pengguna
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class PenggunaRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun getPenggunas(): List<Pengguna> = withContext(Dispatchers.IO) {
        val snapshot = Tasks.await(
            db.collection(USERS)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
        )
        snapshot.documents.map { it.toPengguna() }
    }

    suspend fun updateStatusPengguna(id: String, status: String): Boolean = withContext(Dispatchers.IO) {
        val accountStatus = statusToFirestore(status)
        Tasks.await(
            db.collection(USERS).document(id).update(
                mapOf(
                    "accountStatus" to accountStatus,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            )
        )
        SystemLogger.logActivity("Updated status of Pengguna (ID: $id) to $status")
        true
    }

    private fun DocumentSnapshot.toPengguna(): Pengguna {
        val role = when ((getString("role") ?: ROLE_USER).uppercase()) {
            ROLE_ADMIN -> "Admin"
            else -> "User"
        }

        val status = when ((getString("accountStatus") ?: ACCOUNT_ACTIVE).uppercase()) {
            ACCOUNT_DISABLED -> "Diblokir"
            else -> "Aktif"
        }

        return Pengguna(
            id = id,
            namaLengkap = getString("fullName") ?: "",
            email = getString("email") ?: "",
            nomorTelepon = getString("phoneNumber") ?: "",
            role = role,
            status = status,
            tanggalDaftar = getTimestamp("createdAt").toUiDate()
        )
    }

    private fun com.google.firebase.Timestamp?.toUiDate(): String {
        if (this == null) return ""
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("id-ID"))
        return formatter.format(toDate())
    }

    private fun statusToFirestore(status: String): String {
        return when (status.trim().lowercase()) {
            "diblokir", "ban", "banned", "disabled" -> ACCOUNT_DISABLED
            else -> ACCOUNT_ACTIVE
        }
    }

    private companion object {
        const val USERS = "users"
        const val ROLE_ADMIN = "ADMIN"
        const val ROLE_USER = "USER"
        const val ACCOUNT_ACTIVE = "ACTIVE"
        const val ACCOUNT_DISABLED = "DISABLED"
    }
}
