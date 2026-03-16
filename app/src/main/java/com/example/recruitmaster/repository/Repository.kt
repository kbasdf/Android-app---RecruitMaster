package com.example.recruitmaster.repository

import com.example.recruitmaster.network.ApiService
import com.example.recruitmaster.model.SessionPage

class SessionRepository(private val api: ApiService) {
    suspend fun fetchPage(id: Int): SessionPage {
        return api.getSessionPage(id)
    }
}
