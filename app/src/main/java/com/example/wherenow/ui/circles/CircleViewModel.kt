package com.example.wherenow.ui.circles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.repository.CircleRepository
import kotlinx.coroutines.launch

class CircleViewModel(
    private val repo: CircleRepository = CircleRepository()
) : ViewModel() {

    fun createCircle(
        name: String,
        desc: String,
        categoryId: String,
        creatorId: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            repo.createCircle(
                name = name,
                description = desc,
                category = categoryId,
                creatorId = creatorId,
                onResult = onResult
            )
        }
    }
}