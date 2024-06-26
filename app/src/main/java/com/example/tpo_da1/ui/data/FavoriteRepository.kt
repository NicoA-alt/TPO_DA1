package com.example.tpo_da1.ui.data

import com.example.tpo_da1.ui.domain.Deal
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FavoriteRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getFavorites(): List<Deal> {
        return try {
            val result = db.collection("favorites").get().await()
            result.map { document -> document.toObject(Deal::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}