package com.app.rrq.model.repository

import com.app.rrq.model.data.Pengguna
import com.app.rrq.core.utils.SystemLogger

class PenggunaRepository {
    // Mock data for Pengguna since there is no API yet
    private val mockPenggunas = mutableListOf(
        Pengguna("1", "Admin Satu", "admin@roadresq.com", "081234567890", "Admin", "Aktif"),
        Pengguna("2", "User Budi", "budi@gmail.com", "081234567891", "User", "Aktif"),
        Pengguna("3", "User Siti", "siti@gmail.com", "081234567892", "User", "Diblokir")
    )

    suspend fun getPenggunas(): List<Pengguna> {
        kotlinx.coroutines.delay(1000)
        return mockPenggunas.toList()
    }

    suspend fun updateStatusPengguna(id: String, status: String): Boolean {
        kotlinx.coroutines.delay(1000)
        val index = mockPenggunas.indexOfFirst { it.id == id }
        if (index != -1) {
            val updated = mockPenggunas[index].copy(status = status)
            mockPenggunas[index] = updated
            SystemLogger.logActivity("Updated status of Pengguna (ID: $id) to $status")
            return true
        }
        return false
    }
}
