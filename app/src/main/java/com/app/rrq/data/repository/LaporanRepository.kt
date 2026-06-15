package com.app.rrq.data.repository

import com.app.rrq.data.model.Laporan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class LaporanRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val laporanCollection = db.collection("laporan")
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun kirimLaporan(
        laporan: Laporan,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onError("User belum login")
            return
        }
        val data = laporan.copy(userId = userId)
        laporanCollection
            .add(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Gagal mengirim laporan") }
    }

    suspend fun simpanLaporan(laporan: Laporan): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("User belum login"))
            val docRef = laporanCollection.document()
            val laporanWithId = laporan.copy(id = docRef.id, userId = userId)
            docRef.set(laporanWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getSemuaLaporan(onResult: (List<Laporan>) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onResult(emptyList())
            return
        }
        laporanCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                val list = snapshot?.toObjects(Laporan::class.java) ?: emptyList()
                val sortedList = list.sortedByDescending { laporan ->
                    try { dateFormat.parse(laporan.tanggal) } catch (e: Exception) { null }
                }
                onResult(sortedList)
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