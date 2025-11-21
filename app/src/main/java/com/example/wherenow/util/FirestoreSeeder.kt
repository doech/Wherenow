package com.example.wherenow.util

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreSeeder {

    private val db = FirebaseFirestore.getInstance()

    private val categories = listOf(
        mapOf(
            "id" to "music",
            "icon" to "music",
            "color" to "#A855F7",
            "status" to "active",
            "name" to mapOf("es" to "Música", "en" to "Music")
        ),
        mapOf(
            "id" to "parties",
            "icon" to "social",
            "color" to "#F93457",
            "status" to "active",
            "name" to mapOf("es" to "Fiestas", "en" to "Parties")
        ),
        mapOf(
            "id" to "sports",
            "icon" to "sports",
            "color" to "#3B82F6",
            "status" to "active",
            "name" to mapOf("es" to "Deportes", "en" to "Sports")
        ),
        mapOf(
            "id" to "nightlife",
            "icon" to "social",
            "color" to "#FF4081",
            "status" to "active",
            "name" to mapOf("es" to "Vida Nocturna", "en" to "Nightlife")
        ),
        mapOf(
            "id" to "concerts",
            "icon" to "music",
            "color" to "#7C4DFF",
            "status" to "active",
            "name" to mapOf("es" to "Conciertos", "en" to "Concerts")
        ),
        mapOf(
            "id" to "workshops",
            "icon" to "learning",
            "color" to "#4CAF50",
            "status" to "active",
            "name" to mapOf("es" to "Talleres", "en" to "Workshops")
        ),
        mapOf(
            "id" to "bar_meetups",
            "icon" to "social",
            "color" to "#FF9800",
            "status" to "active",
            "name" to mapOf("es" to "Reuniones en Bares", "en" to "Bar Meetups")
        ),
        mapOf(
            "id" to "food_experiences",
            "icon" to "food",
            "color" to "#FF5722",
            "status" to "active",
            "name" to mapOf("es" to "Experiencias Gastronómicas", "en" to "Food Experiences")
        ),
        mapOf(
            "id" to "art_exhibitions",
            "icon" to "arts",
            "color" to "#9C27B0",
            "status" to "active",
            "name" to mapOf("es" to "Exhibiciones de Arte", "en" to "Art Exhibitions")
        ),
        mapOf(
            "id" to "volunteering",
            "icon" to "health",
            "color" to "#009688",
            "status" to "active",
            "name" to mapOf("es" to "Voluntariado", "en" to "Volunteering")
        ),
        mapOf(
            "id" to "gaming_events",
            "icon" to "gaming",
            "color" to "#3F51B5",
            "status" to "active",
            "name" to mapOf("es" to "Eventos Gamer", "en" to "Gaming Events")
        ),
        mapOf(
            "id" to "coffee_parties",
            "icon" to "food",
            "color" to "#A1887F",
            "status" to "active",
            "name" to mapOf("es" to "Cafecitos", "en" to "Coffee Parties")
        )
    )

    fun seedCategories(onDone: (Boolean) -> Unit) {
        val batch = db.batch()

        categories.forEach { cat ->
            val doc = db.collection("categories").document(cat["id"] as String)
            batch.set(doc, cat)
        }

        batch.commit()
            .addOnSuccessListener { onDone(true) }
            .addOnFailureListener { onDone(false) }
    }
}
