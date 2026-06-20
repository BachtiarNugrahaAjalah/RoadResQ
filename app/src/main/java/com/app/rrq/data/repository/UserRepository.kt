package com.app.rrq.data.repository

import com.app.rrq.data.model.User
import com.app.rrq.data.model.Role
import com.app.rrq.data.model.Status
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    fun getSemuaUser(onResult: (List<User>) -> Unit): ListenerRegistration {
        return usersCollection
            .whereEqualTo("role", Role.USER.name)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                val list = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        val userId = doc.id // Mengambil ID dokumen
                        val name = doc.getString("name") ?: ""
                        val email = doc.getString("email") ?: ""
                        val phone = doc.getString("phone") ?: ""
                        val date = doc.getString("date") ?: ""
                        val roleStr = doc.getString("role") ?: Role.USER.name
                        val statusStr = doc.getString("status") ?: Status.AKTIF.name
                        
                        User(
                            userId = userId,
                            name = name,
                            email = email,
                            phone = phone,
                            date = date,
                            role = Role.valueOf(roleStr),
                            status = Status.valueOf(statusStr)
                        )
                    } catch (_: Exception) {
                        null
                    }
                } ?: emptyList()
                onResult(list)
            }
    }

    suspend fun banUser(email: String, currentStatus: Status): Result<Unit> {
        return try {
            val newStatus = if (currentStatus == Status.AKTIF) Status.BANNED else Status.AKTIF
            val snapshot = usersCollection.whereEqualTo("email", email).get().await()
            for (doc in snapshot.documents) {
                doc.reference.update("status", newStatus.name).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser(email: String): Result<Unit> {
        return try {
            val snapshot = usersCollection.whereEqualTo("email", email).get().await()
            for (doc in snapshot.documents) {
                doc.reference.delete().await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
