package com.example.wherenow.data.model

data class SearchResult(
    val id: String,
    val name: String,
    val type: String // "event", "circle", "user"
)