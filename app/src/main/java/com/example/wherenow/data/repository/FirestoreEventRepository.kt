package com.example.wherenow.data.repository

import com.example.wherenow.data.model.EventRow
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FieldValue
import com.example.wherenow.data.model.JoinRequest
import android.util.Log


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
                priceText    = doc.getString("priceText") ?: "",
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
            "eventId"      to docRef.id,
            "name"         to name,
            "description"  to "",
            "location"     to "",
            "distanceText" to "",
            "priceText"    to "Free",   // ahora String
            "interested"   to 0,
            "status"       to status,
            "createdAt"    to now,
            "startAt"      to now
        )

        docRef.set(data).await()
        return docRef.id
    }

    suspend fun getById(eventId: String): EventRow? {
        val doc = db.collection("events").document(eventId)
            .get()
            .await()

        if (!doc.exists()) return null

        val createdTs = doc.getTimestamp("createdAt")
        val startTs   = doc.getTimestamp("startAt")

        return EventRow(
            eventId      = doc.id,
            name         = doc.getString("name") ?: "",
            description  = doc.getString("description") ?: "",
            location     = doc.getString("location") ?: "",
            distanceText = doc.getString("distanceText") ?: "",
            priceText    = doc.getString("priceText") ?: "",
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

    override suspend fun sendJoinRequest(eventId: String, userId: String) {
        val data = mapOf(
            "userId" to userId,
            "status" to "pending",
            "requestedAt" to FieldValue.serverTimestamp()
        )

        FirebaseFirestore.getInstance()
            .collection("events")
            .document(eventId)
            .collection("join_requests")
            .document(userId)
            .set(data)
    }

    override suspend fun acceptJoinRequest(eventId: String, userId: String) {
        val eventRef = db.collection("events").document(eventId)

        // 1. Agregar relación en /users/{userId}/events
        db.collection("users")
            .document(userId)
            .collection("events")
            .document(eventId)
            .set(
                mapOf(
                    "eventId" to eventId,
                    "role" to "participant",
                    "joinedAt" to FieldValue.serverTimestamp()
                )
            ).await()

        // 2. Opcional: agregar en /events/{eventId}/members
        eventRef.collection("members")
            .document(userId)
            .set(
                mapOf(
                    "userId" to userId,
                    "joinedAt" to FieldValue.serverTimestamp()
                )
            ).await()

        // 3. Eliminar solicitud
        eventRef.collection("join_requests")
            .document(userId)
            .delete()
            .await()
    }

    override suspend fun rejectJoinRequest(eventId: String, userId: String) {
        db.collection("events")
            .document(eventId)
            .collection("join_requests")
            .document(userId)
            .delete()
            .await()
    }

    suspend fun getAllJoinRequestsForOwner(ownerId: String?): List<JoinRequest> {
        Log.d("JOIN_DEBUG", "===================== DEBUG START =====================")

        // 0. Validación del ownerId
        Log.d("JOIN_DEBUG", "OwnerId recibido = '$ownerId'")
        if (ownerId == null) {
            Log.e("JOIN_DEBUG", "❌ ERROR: ownerId == null")
            return emptyList()
        }
        if (ownerId.isBlank()) {
            Log.e("JOIN_DEBUG", "❌ ERROR: ownerId está vacío o en blanco")
            return emptyList()
        }

        Log.d("JOIN_DEBUG", "🔍 Buscando eventos donde ownerId == '$ownerId'")

        val result = mutableListOf<JoinRequest>()

        try {
            // 1. Mostrar TODOS los eventos
            val allEvents = db.collection("events").get().await()
            Log.d("JOIN_DEBUG", "📌 Total eventos en Firestore: ${allEvents.size()}")

            for (doc in allEvents.documents) {
                val evId = doc.id
                val evOwner = doc.getString("ownerId")
                Log.d("JOIN_DEBUG", " → Evento: $evId | ownerId='$evOwner' | match=${evOwner == ownerId}")
            }

            Log.d("JOIN_DEBUG", "-------------------------------------------------------")

            // 2. Query real
            val eventsQuery = db.collection("events")
                .whereEqualTo("ownerId", ownerId)
                .get()
                .await()

            Log.d("JOIN_DEBUG", "🎯 Eventos donde ownerId == '$ownerId': ${eventsQuery.size()}")

            if (eventsQuery.isEmpty) {
                Log.e("JOIN_DEBUG", "❌ Ningún evento coincide con ownerId.")
                Log.d("JOIN_DEBUG", "===================== DEBUG END =====================")
                return emptyList()
            }

            // 3. Revisar cada evento
            for (eventDoc in eventsQuery.documents) {
                val eventId = eventDoc.id
                val eventName = eventDoc.getString("name") ?: "Unnamed Event"

                Log.d("JOIN_DEBUG", "📌 Revisando eventId=$eventId ($eventName)")

                val requestsQuery = eventDoc.reference
                    .collection("join_requests")
                    .get()
                    .await()

                Log.d("JOIN_DEBUG", " → Requests encontrados: ${requestsQuery.size()}")

                for (req in requestsQuery.documents) {
                    val userId = req.getString("userId")

                    // ⭐ CORREGIDO: Lectura segura de Timestamp / Long / Double
                    val requestedAt: Long = when (val raw = req.get("requestedAt")) {
                        is com.google.firebase.Timestamp -> raw.toDate().time
                        is Long -> raw
                        is Double -> raw.toLong()
                        is String -> {
                            Log.e("JOIN_DEBUG", "requestedAt es String y no Timestamp/Long: $raw")
                            0L
                        }
                        else -> {
                            Log.e("JOIN_DEBUG", "requestedAt tiene tipo desconocido: $raw")
                            0L
                        }
                    }

                    Log.d(
                        "JOIN_DEBUG",
                        "    Request: ${req.id} | userId=$userId | requestedAt=$requestedAt"
                    )

                    if (userId == null) {
                        Log.e("JOIN_DEBUG", "    ❌ Request con userId nulo")
                        continue
                    }

                    val userDoc = db.collection("users")
                        .document(userId)
                        .get()
                        .await()

                    val userName = userDoc.getString("name") ?: "Unknown User"

                    result.add(
                        JoinRequest(
                            requestId = req.id,
                            eventId = eventId,
                            eventName = eventName,
                            userId = userId,
                            userName = userName,
                            requestedAt = requestedAt
                        )
                    )
                }
            }

            Log.d("JOIN_DEBUG", "-------------------------------------------------------")
            Log.d("JOIN_DEBUG", "📊 Total requests generados: ${result.size}")
            result.forEach {
                Log.d("JOIN_DEBUG", " → $it")
            }

        } catch (e: Exception) {
            Log.e("JOIN_DEBUG", "🔥 ERROR ejecutando la función: ${e.message}", e)
        }

        Log.d("JOIN_DEBUG", "===================== DEBUG END =====================")

        return result.sortedByDescending { it.requestedAt }
    }




}
