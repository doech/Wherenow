package com.example.wherenow.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.model.SearchResult
import com.example.wherenow.data.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Usa Firebase real (solo eventos)
private const val USE_FAKE_DATA = false

class SearchViewModel(
    private val repository: SearchRepository = SearchRepository()
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
        if (newQuery.isNotBlank()) {
            fetchSearchResults(newQuery)
        } else {
            _searchResults.value = emptyList()
        }
    }

    fun updateFilter(filter: String) {
        _selectedFilter.value = filter
        if (_query.value.isNotBlank()) {
            fetchSearchResults(_query.value)
        }
    }

    fun fetchSearchResults(q: String) {
        val query = q.trim()
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (USE_FAKE_DATA) {
                    // ===== Modo fake (como lo tenías) =====
                    val fakeResults = listOf(
                        SearchResult("1", "Summer Party", "event"),
                        SearchResult("2", "Photography Club", "circle"),
                        SearchResult("3", "María González", "user"),
                        SearchResult("4", "Tech Meetup", "event"),
                        SearchResult("5", "Hiking Crew", "circle")
                    ).filter { it.name.contains(query, ignoreCase = true) }
                        .filter {
                            when (_selectedFilter.value) {
                                "All" -> true
                                "Events" -> it.type == "event"
                                "Circles" -> it.type == "circle"
                                "People" -> it.type == "user"
                                else -> true
                            }
                        }

                    _searchResults.value = fakeResults
                } else {
                    // ===== Modo real: SOLO eventos en Firestore =====
                    val results = when (_selectedFilter.value) {
                        "All", "Events" -> repository.searchEventsFirebase(query)
                        "Circles", "People" -> emptyList()  // aún no implementado
                        else -> emptyList()
                    }
                    _searchResults.value = results
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}
