package com.example.coincapappjp.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend fun addFavoriteForUser(
        uid: String,
        assetId: String
    ): Result<Unit> {
        return try {
            db.collection("favourites")
                .document(uid)
                .update("favourites", FieldValue.arrayUnion(assetId))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getFavouritesForUser(uid: String): List<String> {
        return try {
            val doc = db.collection("favourites").document(uid).get().await()
            val list = doc.get("favourites") as? List<String>
            list ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
