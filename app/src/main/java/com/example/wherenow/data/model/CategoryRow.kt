package com.example.wherenow.data.model

import com.google.firebase.Timestamp

data class CategoryRow(
    val id: String = "",
    val name: Map<String, String> = emptyMap(), // { "en": "...", "es": "..." }
    val color: String = "#000000",              // Hex color
    val icon: String = "",                      // Nombre del ícono
    val status: String = "active",
    val createdAt: Timestamp? = null
)