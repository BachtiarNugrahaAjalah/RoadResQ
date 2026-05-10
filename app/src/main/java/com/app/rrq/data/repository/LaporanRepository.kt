package com.app.rrq.data.repository
import com.app.rrq.data.model.Laporan
import com.app.rrq.data.api.RetrofitClient

class LaporanRepository {
    suspend fun getLaporans(): List<Laporan> {
        return try {
            RetrofitClient.instance.getLaporans()
        } catch (e: Exception) {
            emptyList()
        }
    }
}