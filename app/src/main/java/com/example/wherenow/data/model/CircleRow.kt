package com.example.wherenow.data.model

import java.util.Date

data class CircleRow(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val creatorId: String = "",
    val visibility: String = "public",   // public / private
    val status: String = "active",
    val membersCount: Int = 0,
    val createdAt: Date? = null,         // Firestore timestamp
    val lastActivity: Date? = null       // Firestore timestamp
)
