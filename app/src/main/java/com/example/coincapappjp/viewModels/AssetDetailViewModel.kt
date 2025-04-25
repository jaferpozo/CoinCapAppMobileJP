// AssetDetailViewModel.kt
package com.example.coincapappjp.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coincapappjp.models.Asset
import com.example.coincapappjp.data.FirestoreRepository
import com.example.coincapappjp.data.AuthRepository
import com.example.coincapappjp.services.CoinCapApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AssetDetailUiState(
    val loading: Boolean = false,
    val asset: Asset? = null,
    val error: String? = null,
    val addResult: Result<Unit>? = null
)

@HiltViewModel
class AssetDetailViewModel @Inject constructor(
    private val apiService: CoinCapApiService,
    private val fsRepo: FirestoreRepository,
    private val authRepo: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val assetId   = savedStateHandle.get<String>("assetId")!!
    private val assetName = savedStateHandle.get<String>("assetName")!!

    private val _uiState = MutableStateFlow(AssetDetailUiState())
    val uiState: StateFlow<AssetDetailUiState> = _uiState.asStateFlow()

    init { loadAsset() }

    private fun loadAsset() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            try {
                val all = apiService.getAssets().data
                val dto = all.firstOrNull { it.id == assetId }
                if (dto == null) {
                    throw Exception("Activo no encontrado")
                }
                val asset = Asset(
                    id = dto.id,
                    name = assetName,
                    symbol = dto.symbol,
                    price = String.format("%.2f", dto.priceUsd.toDouble()),
                    percentage = String.format("%.2f", dto.changePercent24Hr.toDouble()).toDouble()
                )
                _uiState.update { it.copy(loading = false, asset = asset) }
            } catch (e: Exception) {
                _uiState.update { it.copy(loading = false, error = e.message) }
            }
        }
    }

    fun addToFavorites() {
        viewModelScope.launch {
            _uiState.update { it.copy(addResult = null) }
            val user = authRepo.getCurrentUser()
            if (user == null) {
                _uiState.update {
                    it.copy(addResult = Result.failure(Exception("No hay usuario autenticado")))
                }
                return@launch
            }
            uiState.value.asset?.let { asset ->
                val res = fsRepo.addFavoriteForUser(
                    uid = user.uid,
                    assetId   = asset.id,

                )
                _uiState.update { it.copy(addResult = res) }
            }
        }
    }
    fun clearAddResult() {
        _uiState.update { it.copy(addResult = null) }
    }

}
