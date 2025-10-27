package com.example.wherenow.ui.search

data class SearchResult(
    val id: String,
    val name: String,
    val type: String // "event", "circle", "user"
)