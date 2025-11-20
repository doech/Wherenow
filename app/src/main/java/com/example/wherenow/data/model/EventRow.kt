package com.example.wherenow.data.model

import java.util.Date

data class EventRow(
    val eventId: String = "",
    val name: String = "",
    val description: String = "",
    val location: String = "",
    val distanceText: String = "",
    val priceText: String = "",   // ← (antes Int, ahora String)
    val interested: Int = 0,
    val status: String = "active",
    val createdAt: Date? = null,  // marca de tiempo Firestore → Date?
    val startAt: Date? = null     // marca de tiempo Firestore → Date?
)
