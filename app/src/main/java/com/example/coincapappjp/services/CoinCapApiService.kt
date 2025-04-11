package com.example.coincapappjp.services

import com.example.coincapappjp.models.AssetsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class CoinCapApiService @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getAssets(): AssetsResponse {
        val response: HttpResponse = client.get(urlString = "https://rest.coincap.io/v3/assets?apiKey=5afe9a3cc58cd3026d29a4dd7cec05ef0e1cef5a50072b547d8bcd62785ab287")
        return response.body()
    }
}