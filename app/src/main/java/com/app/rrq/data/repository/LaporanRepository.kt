package com.app.rrq.data.repository

import com.app.rrq.data.model.Laporan
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class LaporanRepository {
    private val db = FirebaseFirestore.getInstance()
    private val laporanCollection = db.collection("laporan")
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    suspend fun simpanLaporan(laporan: Laporan): Result<Unit> {
        return try {
            val docRef = laporanCollection.document()
            val laporanWithId = laporan.copy(id = docRef.id)
            docRef.set(laporanWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getSemuaLaporan(onResult: (List<Laporan>) -> Unit) {
        laporanCollection.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            val list = snapshot?.toObjects(Laporan::class.java) ?: emptyList()
            
            // Urutkan list berdasarkan tanggal (Terbaru di atas)
            val sortedList = list.sortedByDescending { laporan ->
                try {
                    dateFormat.parse(laporan.tanggal)
                } catch (e: Exception) {
                    null
                }
            }
            
            onResult(sortedList)
        }
    }

    suspend fun updateStatusLaporan(laporanId: String, statusBaru: String, statusAdmin: String): Result<Unit> {
        return try {
            laporanCollection.document(laporanId)
                .update(
                    "Status", statusBaru,
                    "StatusAdmin", statusAdmin
                ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
