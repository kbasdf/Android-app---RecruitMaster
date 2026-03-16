package com.example.recruitmaster.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recruitmaster.model.SessionPage
import com.example.recruitmaster.network.ApiClient
import com.example.recruitmaster.repository.SessionRepository
import kotlinx.coroutines.launch
import android.util.Log

class SessionViewModel(
    private val repository: SessionRepository = SessionRepository(ApiClient.apiService)
) : ViewModel() {

    var pages by mutableStateOf(
        listOf(
            SessionPage("Welcome", "Hardcoded text..."),
            SessionPage("Purpose", "Hardcoded text..."),
            SessionPage("Basics", "Hardcoded text..."),
            SessionPage("Page 4"), // blank initially
            SessionPage("Page 5")  // blank initially
        )
    )
        private set

    fun fetchPage(index: Int) {
        val current = pages[index]
        if (current.description == null) {
            viewModelScope.launch {
                try {
                    val fetched = repository.fetchPage(index + 1)
                    pages = pages.toMutableList().apply {
                        this[index] = this[index].copy(description = fetched.description)
                    }
                    Log.d("SessionViewModel", "Fetched page $index successfully: ${fetched.description}")
                } catch (e: Exception) {
                    Log.e("SessionViewModel", "Error fetching page $index", e)
                }
            }
        }
    }
}
