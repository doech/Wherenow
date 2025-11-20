package com.example.wherenow.data.repository

import com.example.wherenow.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    fun createUser(profile: UserProfile, onResult: (Boolean) -> Unit) {
        db.collection("users")
            .document(profile.id)
            .set(profile)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getUser(uid: String, onResult: (UserProfile?) -> Unit) {
        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                onResult(doc.toObject(UserProfile::class.java))
            }
            .addOnFailureListener { onResult(null) }
    }
}
