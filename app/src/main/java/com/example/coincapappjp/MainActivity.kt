package com.example.coincapappjp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.coincapappjp.data.AuthRepository
import com.example.coincapappjp.ui.theme.CoinCapAppJPTheme
import com.example.coincapappjp.views.MainScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoinCapAppJPTheme {
                val repo = remember {
                    AuthRepository(
                        auth = FirebaseAuth.getInstance(),
                    )
                }
                repo.signOut()
                MainScreen()
            }
        }
    }
}