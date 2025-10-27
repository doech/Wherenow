package com.example.wherenow.ui.search

/**
 * SearchRepository temporal sin Firebase.
 * MVP funcional con datos locales simulados.
 * Tu compañera podrá conectar Firebase fácilmente luego
 * editando la función fetchFromFirebase().
 */
class SearchRepository {

    suspend fun searchEvents(query: String): List<SearchResult> {
        if (query.isBlank()) return emptyList()
        val fakeEvents = listOf(
            SearchResult("1", "Summer Party", "event"),
            SearchResult("2", "Tech Meetup", "event"),
            SearchResult("3", "Festival UVG", "event")
        )
        return fakeEvents.filter { it.name.contains(query, ignoreCase = true) }
    }

    suspend fun searchCircles(query: String): List<SearchResult> {
        if (query.isBlank()) return emptyList()
        val fakeCircles = listOf(
            SearchResult("4", "Photography Club", "circle"),
            SearchResult("5", "Gaming Squad", "circle"),
            SearchResult("6", "Study Buddies", "circle")
        )
        return fakeCircles.filter { it.name.contains(query, ignoreCase = true) }
    }

    suspend fun searchUsers(query: String): List<SearchResult> {
        if (query.isBlank()) return emptyList()
        val fakeUsers = listOf(
            SearchResult("7", "María González", "user"),
            SearchResult("8", "Carlos Pérez", "user"),
            SearchResult("9", "Laura Martínez", "user")
        )
        return fakeUsers.filter { it.name.contains(query, ignoreCase = true) }
    }

    /**
     * 🔌 Función de integración futura con Firebase.
     * Aquí tu compañera podrá implementar la conexión real.
     */
    suspend fun fetchFromFirebase(query: String): List<SearchResult> {
        // TODO: Implementar conexión con Firebase Firestore
        // Ejemplo futuro:
        // val snapshot = FirebaseFirestore.getInstance().collection("events").get().await()
        // ...
        return emptyList()
    }
}
