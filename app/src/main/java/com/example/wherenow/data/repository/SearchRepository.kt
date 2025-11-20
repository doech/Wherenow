package com.example.wherenow.data.repository

import com.example.wherenow.data.model.SearchResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SearchRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun searchEventsFirebase(query: String): List<SearchResult> {
        if (query.isBlank()) return emptyList()

        val snap = db.collection("events")
            .limit(50)
            .get()
            .await()

        return snap.documents.mapNotNull { d ->
            val name = d.getString("name") ?: return@mapNotNull null
            if (name.contains(query, ignoreCase = true)) {
                SearchResult(id = d.id, name = name, type = "event")
            } else null
        }
    }
}
