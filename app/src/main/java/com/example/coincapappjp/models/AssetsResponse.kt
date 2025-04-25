package com.example.coincapappjp.models

import kotlinx.serialization.Serializable

@Serializable
data class AssetsResponse (
    val data: List<AssetResponse>
)
@Serializable
data class AssetsResponseFavourites (
    val data: AssetResponse
)
