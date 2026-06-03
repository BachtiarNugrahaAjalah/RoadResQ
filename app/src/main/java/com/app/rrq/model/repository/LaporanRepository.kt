package com.app.rrq.model.repository

import com.app.rrq.model.data.Laporan
import com.app.rrq.core.api.RetrofitClient
import com.app.rrq.core.utils.SystemLogger

class LaporanRepository {
    suspend fun getLaporans(): List<Laporan> {
        return try {
            RetrofitClient.instance.getLaporans()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getLaporansByUser(userId: String): List<Laporan> {
        // Mock getting by user (e.g., matching some logic, or just return all for mockup)
        val laporans = getLaporans()
        // For now, since there's no actual userId in Laporan JSON currently provided,
        // we will just return a mocked subset or all
        return laporans
    }

    suspend fun getLaporanById(id: String): Laporan? {
        // Mock getting by ID (we fetch all and find the one with matching ID)
        val laporans = getLaporans()
        return laporans.find { it.id == id }
    }

    suspend fun addLaporan(judul: String, kategori: String, urgensi: String, lokasi: String, deskripsi: String): Boolean {
        // Simulate network delay for posting
        kotlinx.coroutines.delay(2000)
        SystemLogger.logActivity("Added new Laporan: $judul")
        return true
    }

    suspend fun updateStatusLaporan(id: String, status: String): Boolean {
        // Simulate network delay for updating
        kotlinx.coroutines.delay(1000)
        SystemLogger.logActivity("Updated status of Laporan (ID: $id) to $status")
        return true
    }
}