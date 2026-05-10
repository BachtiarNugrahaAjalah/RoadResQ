package com.app.rrq.data.api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL ="https://gist.githubusercontent.com/BachtiarNugrahaAjalah/2a51d525a0a374af937f0422bb91f920/raw/fbecd8d98ae8018338c873d944345692d7c5636b/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}