package com.example.wherenow.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.model.EventRow
import com.example.wherenow.data.repository.EventRepository
import com.example.wherenow.data.repository.FirestoreEventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventsViewModel(
    private val repo: EventRepository = FirestoreEventRepository()
) : ViewModel() {

    private val _events = MutableStateFlow<List<EventRow>>(emptyList())
    val events: StateFlow<List<EventRow>> = _events

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadEvents() = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        try { _events.value = repo.getAll() }
        catch (e: Exception) { _error.value = e.message }
        finally { _loading.value = false }
    }

    // ejemplo para crear rápido un evento
    fun addSample() = viewModelScope.launch {
        repo.addEvent(name = "Sample Event")
        loadEvents()
    }
}

