package com.example.wherenow.data.repository

import com.example.wherenow.data.model.EventRow

interface EventRepository {
    suspend fun getAll(): List<EventRow>

    suspend fun addEvent(
        name: String,
        eventId: String = "",
        status: String = "active"
    ): String

    suspend fun setStatus(eventId: String, status: String)

    suspend fun sendJoinRequest(eventId: String, userId: String)

    suspend fun acceptJoinRequest(eventId: String, userId: String)
    suspend fun rejectJoinRequest(eventId: String, userId: String)
}
