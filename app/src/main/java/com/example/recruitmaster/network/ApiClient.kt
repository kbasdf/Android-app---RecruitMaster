package com.example.recruitmaster.network
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.29.42:2200/") // emulator localhost
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}