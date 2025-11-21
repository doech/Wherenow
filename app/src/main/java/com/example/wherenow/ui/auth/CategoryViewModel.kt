package com.example.wherenow.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.model.CategoryRow
import com.example.wherenow.data.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.wherenow.data.repository.UserRepository

class CategoryViewModel(
    private val repo: CategoryRepository = CategoryRepository(),
    private val userRepo: UserRepository = UserRepository()
) : ViewModel() {

    private val _selectedCategories = MutableStateFlow<List<CategoryRow>>(emptyList())
    val selectedCategories = _selectedCategories.asStateFlow()
    private val _categories = MutableStateFlow<List<CategoryRow>>(emptyList())
    val categories = _categories.asStateFlow()

    fun loadCategories() {
        viewModelScope.launch {
            repo.getAllCategories { list ->
                _categories.value = list
            }
        }
    }

    fun saveUserSelectedCategories(
        userId: String,
        selectedIds: List<String>,
        onComplete: (Boolean) -> Unit = {}
    ) {
        viewModelScope.launch {

            // 1. Filtrar categorías completas
            val selected = _categories.value.filter { selectedIds.contains(it.id) }
            _selectedCategories.value = selected

            // 2. Guardar en Firestore
            userRepo.saveUserCategories(
                userId = userId,
                categoryIds = selectedIds
            ) { ok ->
                onComplete(ok)
            }
        }
    }
}
