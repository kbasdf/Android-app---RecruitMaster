package com.example.recruitmaster.network
import com.example.recruitmaster.model.SessionPage
import retrofit2.http.GET
import retrofit2.http.Path
interface ApiService {
    @GET("session-pages/{id}")
    suspend fun getSessionPage(@Path("id") id: Int): SessionPage
}