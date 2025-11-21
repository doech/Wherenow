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

    fun saveUserCategories(
        userId: String,
        categoryIds: List<String>,
        onResult: (Boolean) -> Unit
    ) {
        val colRef = db.collection("users")
            .document(userId)
            .collection("categories")

        // BORRAMOS primero para evitar duplicados
        colRef.get().addOnSuccessListener { existing ->
            val batch = db.batch()

            // borrar existentes
            for (doc in existing.documents) {
                batch.delete(doc.reference)
            }

            // agregar nuevos
            for (catId in categoryIds) {
                val docRef = colRef.document(catId)
                val data = mapOf("categoryId" to catId)
                batch.set(docRef, data)
            }

            batch.commit()
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }

        }.addOnFailureListener {
            onResult(false)
        }
    }

}
