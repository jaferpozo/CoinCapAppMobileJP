package com.example.coincapappjp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coincapappjp.data.AuthRepository
import com.example.coincapappjp.models.Asset
import com.example.coincapappjp.services.CoinCapApiService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepo: AuthRepository // Para verificar si el usuario está autenticado
) : ViewModel() {

    private val _favourites = MutableStateFlow<List<Asset>>(emptyList())
    val favourites: StateFlow<List<Asset>> = _favourites

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var listenerRegistration: ListenerRegistration? = null

    init {
        fetchFavourites()  // Se llama al principio para cargar los favoritos
    }

    fun fetchFavourites() {
        _isLoading.value = true

        val userId = authRepo.getCurrentUser()?.uid
        if (userId == null) {
            _isLoading.value = false
            return
        }

        // Usando addSnapshotListener para escuchar cambios en tiempo real
        listenerRegistration = firestore.collection("favourites")
            .document(userId)  // Suponiendo que el ID del documento es el userId
            .addSnapshotListener { docSnapshot, exception ->
                if (exception != null) {
                    println("Error al obtener favoritos: ${exception.message}")
                    _isLoading.value = false
                    return@addSnapshotListener
                }

                if (docSnapshot != null && docSnapshot.exists()) {
                    // Si el documento existe y tiene un campo "favourites" con una lista de IDs de activos
                    val favouritesList = docSnapshot.get("favourites") as? List<String> ?: emptyList()
                    fetchAssetsDetails(favouritesList)
                } else {
                    _isLoading.value = false
                }
            }

    }

    private fun fetchAssetsDetails(favouriteIds: List<String>) {



        // Simulación de obtener los detalles de los activos (puedes usar una API o base de datos local)
        val assets = mutableListOf<Asset>()
        for (id in favouriteIds) {
            // Aquí puedes hacer la llamada a la API o usar datos locales para obtener el detalle del activo
            val asset = Asset(id = id, name = "$id", symbol = "SYM", price = "100.00", percentage = 5.0)
            assets.add(asset)
        }

        _favourites.value = assets
        _isLoading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
