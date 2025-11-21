package com.example.wherenow.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class CircleRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    /**
     * Crea un círculo según la estructura de Firestore:
     *
     * circles → {circleId} → {
     *     id: String
     *     name: String
     *     description: String
     *     category: String
     *     creatorId: String
     *     createdAt: Date
     *     lastActivity: Date
     *     membersCount: Int
     *     status: "active"
     *     visibility: "private"
     * }
     */
    fun createCircle(
        name: String,
        description: String,
        category: String,
        creatorId: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val circleId = generateCircleId()
        val now = Date()

        val data = mapOf(
            "id" to circleId,
            "name" to name,
            "description" to description,
            "category" to category,
            "creatorId" to creatorId,
            "createdAt" to now,
            "lastActivity" to now,
            "membersCount" to 0,
            "status" to "active",
            "visibility" to "private"
        )

        db.collection("circles")
            .document(circleId)
            .set(data)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    /** Genera un ID único tipo C001 */
    private fun generateCircleId(): String {
        val random = (100..999).random()
        return "C$random"
    }
}
