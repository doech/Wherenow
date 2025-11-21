package com.example.wherenow.data.model

data class JoinRequest(
    val requestId: String = "",
    val eventId: String = "",
    val eventName: String = "",
    val userId: String = "",
    val userName: String = "",
    val requestedAt: Long = 0L
)
