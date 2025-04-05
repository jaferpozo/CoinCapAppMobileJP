package com.example.coincapappjp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.coincapappjp.models.Asset
import com.example.coincapappjp.ui.theme.CoinCapAppJPTheme
import com.example.coincapappjp.views.AssetRow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoinCapAppJPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (
                        verticalArrangement = Arrangement.Center,
                        modifier= Modifier.fillMaxSize()
                    ) {     AssetRow(
                        asset = Asset(
                            id = "1",
                            name = "Ethereun",
                            price = "$103000",
                            symbol ="ETH",
                            percentage = 2.00,
                        )

                    )
                        AssetRow(
                            asset = Asset(
                                id = "2",
                                name = "BTC",
                                price = "$8000",
                                symbol ="btc",
                                percentage = 0.00,
                            )

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CoinCapAppJPTheme {
        Greeting("Android")
    }
}