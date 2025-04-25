package com.example.coincapappjp.data
import com.example.coincapappjp.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
) {
    suspend fun signIn(email: String, password: String): Resource<String> {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            Resource.Success(user?.uid ?: throw Exception("Usuario no encontrado"))
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
    fun signOut() {
        auth.signOut()
    }
    fun getCurrentUser(): FirebaseUser? = auth.currentUser
}
