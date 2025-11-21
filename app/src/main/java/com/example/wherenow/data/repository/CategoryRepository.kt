package com.example.wherenow.data.repository

import com.example.wherenow.data.model.CategoryRow
import com.google.firebase.firestore.FirebaseFirestore

class CategoryRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    fun getAllCategories(onResult: (List<CategoryRow>) -> Unit) {
        db.collection("categories")
            .get()
            .addOnSuccessListener { snap ->
                val list = snap.documents.mapNotNull { doc ->
                    doc.toObject(CategoryRow::class.java)
                }
                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getCategoryById(id: String, onResult: (CategoryRow?) -> Unit) {
        db.collection("categories")
            .document(id)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    onResult(null)
                    return@addOnSuccessListener
                }
                onResult(doc.toObject(CategoryRow::class.java))
            }
            .addOnFailureListener { onResult(null) }
    }
}
