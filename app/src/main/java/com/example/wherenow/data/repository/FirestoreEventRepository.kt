package com.example.wherenow.data.repository

import com.example.wherenow.data.model.EventRow
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreEventRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : EventRepository {

    override suspend fun getAll(): List<EventRow> {
        val snap = db.collection("events")
            .orderBy("startAt")
            .get()
            .await()

        return snap.documents.map { doc ->
            val createdTs = doc.getTimestamp("createdAt")
            val startTs   = doc.getTimestamp("startAt")
            EventRow(
                eventId      = doc.getString("eventId") ?: doc.id,
                name         = doc.getString("name") ?: "",
                description  = doc.getString("description") ?: "",
                location     = doc.getString("location") ?: "",
                distanceText = doc.getString("distanceText") ?: "",
                priceText    = (doc.getLong("priceText") ?: 0L).toInt(),
                interested   = (doc.getLong("interested") ?: 0L).toInt(),
                status       = doc.getString("status") ?: "active",
                createdAt    = createdTs?.toDate(),
                startAt      = startTs?.toDate()
            )
        }
    }

    override suspend fun addEvent(
        name: String,
        eventId: String,
        status: String
    ): String {
        val docRef = if (eventId.isNotBlank())
            db.collection("events").document(eventId)
        else
            db.collection("events").document()

        val now = Timestamp.now()
        val data = hashMapOf(
            "eventId" to docRef.id,
            "name" to name,
            "description" to "",
            "location" to "",
            "distanceText" to "",
            "priceText" to "Free",
            "interested" to 0,
            "status" to status,        // "active" | "inactive" | "deleted"
            "createdAt" to now,
            "startAt" to now
        )
        docRef.set(data).await()
        return docRef.id
    }

    suspend fun getById(eventId: String): EventRow? {
        val doc = db.collection("events").document(eventId).get().await()
        if (!doc.exists()) return null
        val createdTs = doc.getTimestamp("createdAt")
        val startTs   = doc.getTimestamp("startAt")
        return EventRow(
            eventId      = doc.id,
            name         = doc.getString("name") ?: "",
            description  = doc.getString("description") ?: "",
            location     = doc.getString("location") ?: "",
            distanceText = doc.getString("distanceText") ?: "",
            priceText    = (doc.getLong("priceText") ?: 0L).toInt(),
            interested   = (doc.getLong("interested") ?: 0L).toInt(),
            status       = doc.getString("status") ?: "active",
            createdAt    = createdTs?.toDate(),
            startAt      = startTs?.toDate()
        )
    }


    override suspend fun setStatus(eventId: String, status: String) {
        db.collection("events").document(eventId)
            .update("status", status)
            .await()
    }
}
