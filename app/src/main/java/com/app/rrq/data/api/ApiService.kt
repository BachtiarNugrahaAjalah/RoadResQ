package com.app.rrq.data.api
import com.app.rrq.data.model.Laporan
import retrofit2.http.GET

interface ApiService {
    @GET(value = "Laporan.json")
    suspend fun getLaporans(): List<Laporan>
}