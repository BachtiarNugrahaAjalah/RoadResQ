package com.app.rrq.model.repository

import com.app.rrq.core.utils.SystemLogger
import com.app.rrq.model.data.Laporan
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class LaporanRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun getLaporans(): List<Laporan> = withContext(Dispatchers.IO) {
        val snapshot = Tasks.await(
            db.collection(REPORTS)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
        )
        snapshot.documents.mapNotNull { it.toLaporan() }
    }

    suspend fun getLaporansByUser(userId: String): List<Laporan> = withContext(Dispatchers.IO) {
        val uid = userId.ifBlank { auth.currentUser?.uid.orEmpty() }
        if (uid.isBlank()) return@withContext emptyList()

        val snapshot = Tasks.await(
            db.collection(REPORTS)
                .whereEqualTo("userId", uid)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
        )
        snapshot.documents.mapNotNull { it.toLaporan() }
    }

    suspend fun getLaporanById(id: String): Laporan? = withContext(Dispatchers.IO) {
        val document = Tasks.await(db.collection(REPORTS).document(id).get())
        document.toLaporan()
    }

    suspend fun addLaporan(
        judul: String,
        kategori: String,
        urgensi: String,
        lokasi: String,
        deskripsi: String
    ): Boolean = withContext(Dispatchers.IO) {
        val firebaseUser = auth.currentUser ?: throw Exception("Anda harus login untuk membuat laporan")
        val userDoc = Tasks.await(db.collection(USERS).document(firebaseUser.uid).get())
        val accountStatus = userDoc.getString("accountStatus") ?: ACCOUNT_ACTIVE

        if (accountStatus != ACCOUNT_ACTIVE) {
            throw Exception("Akun Anda sedang dinonaktifkan")
        }

        val reportRef = db.collection(REPORTS).document()
        val searchKeywords = buildSearchKeywords(judul, lokasi, deskripsi)
        val report = mapOf(
            "id" to reportRef.id,
            "userId" to firebaseUser.uid,
            "reporterName" to (userDoc.getString("fullName") ?: ""),
            "reporterEmail" to (userDoc.getString("email") ?: firebaseUser.email.orEmpty()),
            "reporterPhone" to (userDoc.getString("phoneNumber") ?: ""),
            "title" to judul.trim(),
            "category" to categoryToFirestore(kategori),
            "urgency" to urgencyToFirestore(urgensi),
            "location" to mapOf(
                "address" to lokasi.trim(),
                "geoPoint" to null,
                "latitude" to null,
                "longitude" to null,
                "geohash" to null
            ),
            "description" to deskripsi.trim(),
            "imageUrls" to emptyList<String>(),
            "status" to STATUS_WAITING,
            "adminNote" to null,
            "verifiedBy" to null,
            "verifiedAt" to null,
            "completedAt" to null,
            "rejectedAt" to null,
            "searchKeywords" to searchKeywords,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )

        Tasks.await(reportRef.set(report))
        SystemLogger.logActivity("Added new Laporan: $judul", firebaseUser.uid)
        true
    }

    suspend fun updateStatusLaporan(id: String, status: String): Boolean = withContext(Dispatchers.IO) {
        val adminUser = auth.currentUser ?: throw Exception("Anda harus login sebagai admin")
        val reportRef = db.collection(REPORTS).document(id)
        val currentDoc = Tasks.await(reportRef.get())

        if (!currentDoc.exists()) return@withContext false

        val fromStatus = currentDoc.getString("status") ?: STATUS_WAITING
        val toStatus = statusToFirestore(status)
        val updates = mutableMapOf<String, Any?>(
            "status" to toStatus,
            "updatedAt" to FieldValue.serverTimestamp()
        )

        when (toStatus) {
            STATUS_VERIFIED -> {
                updates["verifiedBy"] = adminUser.uid
                updates["verifiedAt"] = FieldValue.serverTimestamp()
            }
            STATUS_DONE -> updates["completedAt"] = FieldValue.serverTimestamp()
            STATUS_REJECTED -> updates["rejectedAt"] = FieldValue.serverTimestamp()
        }

        Tasks.await(reportRef.update(updates))
        val adminDoc = Tasks.await(db.collection(USERS).document(adminUser.uid).get())
        val log = mapOf(
            "fromStatus" to fromStatus,
            "toStatus" to toStatus,
            "changedBy" to adminUser.uid,
            "changedByName" to (adminDoc.getString("fullName") ?: "Admin"),
            "note" to null,
            "createdAt" to FieldValue.serverTimestamp()
        )

        Tasks.await(reportRef.collection(STATUS_LOGS).document().set(log))
        SystemLogger.logActivity("Updated status of Laporan (ID: $id) to $status", adminUser.uid)
        true
    }

    private fun DocumentSnapshot.toLaporan(): Laporan? {
        if (!exists()) return null
        val location = get("location") as? Map<*, *>
        val imageUrls = get("imageUrls") as? List<*>
        val createdAt = getTimestamp("createdAt")
        val status = statusToUiLabel(getString("status") ?: STATUS_WAITING)

        return Laporan(
            id = getString("id") ?: id,
            JudulLaporan = getString("title") ?: "",
            KategoriKerusakan = categoryToUiLabel(getString("category") ?: ""),
            TingkatUrgensi = urgencyToUiLabel(getString("urgency") ?: ""),
            Lokasi = location?.get("address") as? String ?: "",
            Deskripsi = getString("description") ?: "",
            Gambar_url = imageUrls?.firstOrNull() as? String ?: "",
            Status = status,
            Tanggal = createdAt.toUiDate(),
            StatusAdmin = status,
            ReporterName = getString("reporterName") ?: ""
        )
    }

    private fun buildSearchKeywords(vararg values: String): List<String> {
        return values
            .flatMap { value -> value.lowercase().split(Regex("[^a-z0-9]+")) }
            .filter { it.length >= 2 }
            .distinct()
            .take(30)
    }

    private fun categoryToFirestore(value: String): String {
        return when (value.trim().lowercase()) {
            "lubang" -> "LUBANG"
            "retak" -> "RETAK"
            "aspal mengelupas" -> "ASPAL_MENGELUPAS"
            "banjir" -> "BANJIR"
            "penerangan rusak" -> "PENERANGAN_RUSAK"
            "marka pudar" -> "MARKA_PUDAR"
            else -> "LAINNYA"
        }
    }

    private fun categoryToUiLabel(value: String): String {
        return when (value.uppercase()) {
            "LUBANG" -> "Lubang"
            "RETAK" -> "Retak"
            "ASPAL_MENGELUPAS" -> "Aspal Mengelupas"
            "BANJIR" -> "Banjir"
            "PENERANGAN_RUSAK" -> "Penerangan Rusak"
            "MARKA_PUDAR" -> "Marka Pudar"
            else -> "Lainnya"
        }
    }

    private fun urgencyToFirestore(value: String): String {
        return when (value.trim().lowercase()) {
            "rendah" -> "RENDAH"
            "tinggi" -> "TINGGI"
            else -> "SEDANG"
        }
    }

    private fun urgencyToUiLabel(value: String): String {
        return when (value.uppercase()) {
            "RENDAH" -> "Rendah"
            "TINGGI" -> "Tinggi"
            else -> "Sedang"
        }
    }

    private fun statusToFirestore(value: String): String {
        return when (value.trim().lowercase()) {
            "baru", "menunggu", "waiting" -> STATUS_WAITING
            "diverifikasi", "verified" -> STATUS_VERIFIED
            "diproses", "in_progress", "in progress" -> STATUS_IN_PROGRESS
            "selesai", "done" -> STATUS_DONE
            "ditolak", "rejected" -> STATUS_REJECTED
            else -> STATUS_WAITING
        }
    }

    private fun statusToUiLabel(value: String): String {
        return when (value.uppercase()) {
            STATUS_VERIFIED -> "Diverifikasi"
            STATUS_IN_PROGRESS -> "Diproses"
            STATUS_DONE -> "Selesai"
            STATUS_REJECTED -> "Ditolak"
            else -> "Menunggu"
        }
    }

    private fun Timestamp?.toUiDate(): String {
        if (this == null) return ""
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("id-ID"))
        return formatter.format(toDate())
    }

    private companion object {
        const val USERS = "users"
        const val REPORTS = "reports"
        const val STATUS_LOGS = "statusLogs"
        const val ACCOUNT_ACTIVE = "ACTIVE"
        const val STATUS_WAITING = "WAITING"
        const val STATUS_VERIFIED = "VERIFIED"
        const val STATUS_IN_PROGRESS = "IN_PROGRESS"
        const val STATUS_DONE = "DONE"
        const val STATUS_REJECTED = "REJECTED"
    }
}
