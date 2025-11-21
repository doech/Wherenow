package com.example.wherenow.data.model

import java.util.Date

data class UserProfile(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val bio: String = "",
    val language: String = "es",
    val city: String = "",
    val lat: Double? = null,
    val lng: Double? = null,
    val createdAt: Date? = null,
    val status: String = "active"
)
