package com.app.rrq.core.api
import com.app.rrq.model.data.Laporan
import retrofit2.http.GET

interface ApiService {
    @GET(value = "Laporan.json")
    suspend fun getLaporans(): List<Laporan>
}