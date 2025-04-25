package com.example.coincapappjp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coincapappjp.models.UserProfile
import com.example.coincapappjp.data.AuthRepository
import com.example.coincapappjp.utils.Resource
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val userProfile: UserProfile? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }

            when (val res = repo.signIn(_uiState.value.email, _uiState.value.password)) {
                is Resource.Success -> {
                    // Ya autenticado, tomamos email/uid de currentUser
                    val user = repo.getCurrentUser()
                    if (user != null) {
                        val profile = UserProfile(
                            id = user.uid,
                            name = user.displayName.orEmpty(),
                            email = user.email.orEmpty()
                        )
                        _uiState.update {
                            it.copy(loading = false, userProfile = profile)
                        }
                    } else {
                        // rara pero posible
                        _uiState.update {
                            it.copy(
                                loading = false,
                                error = "No pudimos recuperar tu sesión. Vuelve a intentarlo."
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    // mapeo de errores de Auth
                    val friendly = when (val ex = res.exception) {
                        is FirebaseAuthInvalidCredentialsException ->
                            "Las credenciales son inválidas. Revisa tu email y contraseña."
                        is FirebaseAuthInvalidUserException ->
                            "No existe ninguna cuenta con ese correo."
                        else ->
                            "Ocurrió un error al iniciar sesión. Por favor inténtalo de nuevo."
                    }
                    _uiState.update {
                        it.copy(loading = false, error = friendly)
                    }
                }
            }
        }
    }

    fun logout() {
        repo.signOut()
        _uiState.value = LoginUiState()
    }
}
