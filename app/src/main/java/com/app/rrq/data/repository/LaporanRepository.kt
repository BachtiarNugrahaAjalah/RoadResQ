package com.app.rrq.data.repository

import com.app.rrq.data.model.Laporan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class LaporanRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val laporanCollection = db.collection("laporan")
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    suspend fun simpanLaporan(laporan: Laporan): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("User belum login"))
            val docRef = laporanCollection.document()
            val laporanWithId = laporan.copy(
                id = docRef.id, 
                userId = userId,
                timestamp = System.currentTimeMillis()
            )
            docRef.set(laporanWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getSemuaLaporan(onResult: (List<Laporan>) -> Unit): ListenerRegistration? {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onResult(emptyList())
            return null
        }
        return laporanCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                val list = snapshot?.toObjects(Laporan::class.java) ?: emptyList()
                val sortedList = list.sortedWith(
                    compareByDescending<Laporan> { it.timestamp }
                        .thenByDescending { laporan ->
                            try { dateFormat.parse(laporan.tanggal) } catch (_: Exception) { null }
                        }
                )
                onResult(sortedList)
            }
    }

    fun getLaporanTerbaru(onResult: (List<Laporan>) -> Unit): ListenerRegistration? {
        val userId = auth.currentUser?.uid ?: return null
        return laporanCollection
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(3)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    getSemuaLaporan { all -> onResult(all.take(3)) }
                    return@addSnapshotListener
                }
                val list = snapshot?.toObjects(Laporan::class.java) ?: emptyList()
                onResult(list)
            }
    }

    fun getSemuaLaporanAdmin(onResult: (List<Laporan>) -> Unit): ListenerRegistration {
        return laporanCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                val list = snapshot?.toObjects(Laporan::class.java) ?: emptyList()
                val sortedList = list.sortedWith(
                    compareByDescending<Laporan> { it.timestamp }
                        .thenByDescending { laporan ->
                            try { dateFormat.parse(laporan.tanggal) } catch (_: Exception) { null }
                        }
                )
                onResult(sortedList)
            }
    }

    fun getLaporanById(id: String, onResult: (Laporan?) -> Unit): ListenerRegistration {
        return laporanCollection.document(id)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onResult(null)
                    return@addSnapshotListener
                }
                onResult(snapshot?.toObject(Laporan::class.java))
            }
    }

    suspend fun getLaporanByIdOnce(id: String): Laporan? {
        return try {
            val snapshot = laporanCollection.document(id).get().await()
            snapshot.toObject(Laporan::class.java)
        } catch (_: Exception) {
            null
        }
    }

    suspend fun updateStatusLaporan(
        laporanId: String,
        statusBaru: String,
        statusAdmin: String
    ): Result<Unit> {
        return try {
            laporanCollection.document(laporanId)
                .update("Status", statusBaru, "StatusAdmin", statusAdmin)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
