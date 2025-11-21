package com.example.wherenow.util

import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

object FirestoreSeeder {

    private val db = FirebaseFirestore.getInstance()

    // ============================================================
    // ===============      E V E N T S   (3)      =================
    // ============================================================

    private val events = listOf(

        // ---------- EVENTO 1 ----------
        mapOf(
            "eventId" to "EVT001",
            "name" to "Food Truck Festival",
            "description" to "Over 20 gourmet food trucks featuring cuisines from around the world.",
            "location" to "Brooklyn Bridge Park",
            "distanceText" to "1.2 miles away",
            "priceText" to "15",
            "interested" to 89,
            "ownerId" to "FcJGCSIf6KfsknTYDFBeckZgD4D2",
            "createdAt" to Date(),
            "startAt" to Date(System.currentTimeMillis() + 86400000L * 10), // +10 días
            "status" to "active"
        ),

        // ---------- EVENTO 2 ----------
        mapOf(
            "eventId" to "EVT002",
            "name" to "Neon Night Party",
            "description" to "A glowing neon-themed electronic music party until sunrise.",
            "location" to "Warehouse District 14",
            "distanceText" to "3.5 miles away",
            "priceText" to "25",
            "interested" to 142,
            "ownerId" to "Ze51cOn1QQhpkggEKNtu5ILjmB1F3",
            "createdAt" to Date(),
            "startAt" to Date(System.currentTimeMillis() + 86400000L * 3), // +3 días
            "status" to "active"
        ),

        // ---------- EVENTO 3 ----------
        mapOf(
            "eventId" to "EVT003",
            "name" to "Community Soccer Match",
            "description" to "Friendly soccer match open to all ages and skill levels.",
            "location" to "Central Park Field 3",
            "distanceText" to "0.8 miles away",
            "priceText" to "Free",
            "interested" to 52,
            "ownerId" to "RLf0KkG0SqOcD2jjH5UFewY31cw1",
            "createdAt" to Date(),
            "startAt" to Date(System.currentTimeMillis() + 86400000L * 5), // +5 días
            "status" to "active"
        )
    )

    // ============================================================
    // ===============    Seed de Eventos       ====================
    // ============================================================

    fun seedEvents(onDone: (Boolean) -> Unit) {
        val batch = db.batch()

        events.forEach { event ->
            val doc = db.collection("events").document(event["eventId"] as String)
            batch.set(doc, event)
        }

        batch.commit()
            .addOnSuccessListener { onDone(true) }
            .addOnFailureListener { onDone(false) }
    }
}
