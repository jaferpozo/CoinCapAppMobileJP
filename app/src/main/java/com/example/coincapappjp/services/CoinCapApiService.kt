package com.example.coincapappjp.services

import com.example.coincapappjp.models.AssetsResponse
import com.example.coincapappjp.models.AssetsResponseFavourites
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject
class CoinCapApiService @Inject constructor(
    private val client: HttpClient
) {
    val apiKey = ""
    val baseUrl = "https://rest.coincap.io/v3/"
    suspend fun getAssets(): AssetsResponse {
        val response: HttpResponse =
            client.get(urlString =  baseUrl+"assets?apiKey="+apiKey)
        return response.body()
    }
    suspend fun getAssetsByid(assetId:String): AssetsResponseFavourites {
          val url = baseUrl+"assets/"+assetId+"?apiKey="+apiKey
        println("la url del servioc : ${url}")
        val response: HttpResponse =
            client.get(urlString =  url)
        return response.body()
    }
}