package com.example.wherenow.data.repository

import com.example.wherenow.data.model.CircleRow
import com.example.wherenow.data.model.EventRow
import com.example.wherenow.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore

class SearchRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    /** USERS — Orden alfabético **/
    fun getAllUsers(onResult: (List<UserProfile>) -> Unit) {
        db.collection("users")
            .get()
            .addOnSuccessListener { snap ->
                val list = snap.documents
                    .mapNotNull { it.toObject(UserProfile::class.java) }
                    .sortedBy { it.name.lowercase() }

                onResult(list)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    /** EVENTS — Orden por createdAt DESC **/
    fun getAllEvents(onResult: (List<EventRow>) -> Unit) {
        db.collection("events")
            .get()
            .addOnSuccessListener { snap ->
                val list = snap.documents
                    .mapNotNull { it.toObject(EventRow::class.java) }
                    .sortedByDescending { it.createdAt?.time ?: 0L }

                onResult(list)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    /** CIRCLES — Orden por createdAt DESC **/
    fun getAllCircles(onResult: (List<CircleRow>) -> Unit) {
        db.collection("circles")
            .get()
            .addOnSuccessListener { snap ->
                val list = snap.documents
                    .mapNotNull { it.toObject(CircleRow::class.java) }
                    .sortedByDescending { it.createdAt?.time ?: 0L }

                onResult(list)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }
}
