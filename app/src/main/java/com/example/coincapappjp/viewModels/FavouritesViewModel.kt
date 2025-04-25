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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
     val firestore: FirebaseFirestore,
     val authRepo: AuthRepository,
     val apiService: CoinCapApiService
) : ViewModel() {
    val _showNoSessionDialog = MutableStateFlow(false)
    val _favourites = MutableStateFlow<List<Asset>>(emptyList())
    val favourites: StateFlow<List<Asset>> = _favourites
    val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    var listenerRegistration: ListenerRegistration? = null

    init {
        fetchFavourites()
    }

    fun fetchFavourites() {
        _isLoading.value = true
        val userId = authRepo.getCurrentUser()?.uid
        if (userId == null) {
            _isLoading.value = false
            _showNoSessionDialog.value = true
            return
        }

        listenerRegistration = firestore.collection("favourites")
            .document(userId)
            .addSnapshotListener { docSnapshot, exception ->
                if (exception != null) {
                    println("Error al obtener favoritos: ${exception.message}")
                    _isLoading.value = false
                    return@addSnapshotListener
                }
                if (docSnapshot != null && docSnapshot.exists()) {
                    val favouritesList = docSnapshot.get("favourites") as? List<String> ?: emptyList()
                    viewModelScope.launch {
                        try {
                            fetchAssetsDetails(favouritesList)
                        } catch (e: Exception) {
                            println("Error al obtener detalles de activos: ${e.message}")
                            _isLoading.value = false
                        }
                    }
                } else {
                    _isLoading.value = false
                }
            }

    }

      private suspend fun fetchAssetsDetails(favouriteIds: List<String>) {
          val assets = mutableListOf<Asset>()
        for (id in favouriteIds) {
            val dto = apiService.getAssetsByid(id).data
            val asset = Asset(
                id = dto.id,
                name = dto.name,
                symbol = dto.symbol,
                price = String.format("%.2f", dto.priceUsd.toDouble()),
                percentage = String.format("%.2f", dto.changePercent24Hr.toDouble()).toDouble()
            )
            assets.add(asset)
        }
        _favourites.value = assets
        _isLoading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
    fun clearNoSessionDialog() {
        _showNoSessionDialog.value = false
    }
}
