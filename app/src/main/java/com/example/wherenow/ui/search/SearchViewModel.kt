package com.example.wherenow.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.model.UserProfile
import com.example.wherenow.data.model.EventRow
import com.example.wherenow.data.model.CircleRow
import com.example.wherenow.data.repository.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repo: SearchRepository = SearchRepository()
) : ViewModel() {

    private val _users = MutableStateFlow<List<UserProfile>>(emptyList())
    val users = _users.asStateFlow()

    private val _events = MutableStateFlow<List<EventRow>>(emptyList())
    val events = _events.asStateFlow()

    private val _circles = MutableStateFlow<List<CircleRow>>(emptyList())
    val circles = _circles.asStateFlow()

    fun loadData() {
        viewModelScope.launch {

            repo.getAllUsers { list ->
                _users.value = list
            }

            repo.getAllEvents { list ->
                _events.value = list
            }

            repo.getAllCircles { list ->
                _circles.value = list
            }
        }
    }
}
