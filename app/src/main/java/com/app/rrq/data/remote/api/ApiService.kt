package com.app.rrq.data.remote.api

import com.app.rrq.data.model.request.LoginRequest
import com.app.rrq.data.model.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>
}