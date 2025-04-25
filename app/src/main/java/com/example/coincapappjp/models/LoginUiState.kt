package com.example.coincapappjp.models

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val userProfile: UserProfile? = null
)