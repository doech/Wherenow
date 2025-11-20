package com.example.wherenow.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wherenow.data.model.UserProfile
import com.example.wherenow.data.repository.AuthRepository
import com.example.wherenow.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepo: AuthRepository = AuthRepository(),
    private val userRepo: UserRepository = UserRepository()
) : ViewModel() {

    private val _user = MutableStateFlow<UserProfile?>(null)
    val user = _user.asStateFlow()

    init { loadCurrentUser() }

    fun loadCurrentUser() {
        val uid = authRepo.getUid() ?: return
        viewModelScope.launch {
            userRepo.getUser(uid) {
                _user.value = it
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        authRepo.login(email.trim(), password.trim()) { ok, msg ->
            if (ok) loadCurrentUser()
            onResult(ok, msg)
        }
    }

    fun signup(email: String, password: String, username: String, name: String, onResult: (Boolean, String?) -> Unit) {
        authRepo.signUp(email.trim(), password.trim()) { ok, msg ->
            if (!ok) {
                onResult(false, msg)
                return@signUp
            }

            val uid = authRepo.getUid()!!
            val profile = UserProfile(
                id = uid,
                email = email,
                username = username,
                name = name
            )

            userRepo.createUser(profile) { created ->
                if (created) loadCurrentUser()
                onResult(created, null)
            }
        }
    }

    fun logout() {
        authRepo.logout()
        _user.value = null
    }
}
