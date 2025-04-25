package com.example.coincapappjp.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.coincapappjp.viewModels.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SettingsView() {
    val viewModel: SettingsViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        if (state.userProfile == null || user == null ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .widthIn(min = 280.dp, max = 400.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(Modifier.height(16.dp))

                        Text("Iniciar sesión", style = MaterialTheme.typography.titleLarge)

                        Spacer(Modifier.height(24.dp))

                        OutlinedTextField(
                            value = state.email,
                            onValueChange = viewModel::updateEmail,
                            label = { Text("Correo electrónico") },
                            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(16.dp))

                        OutlinedTextField(
                            value = state.password,
                            onValueChange = viewModel::updatePassword,
                            label = { Text("Contraseña") },
                            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(24.dp))

                        Button(
                            onClick = viewModel::login,
                            enabled = !state.loading,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (state.loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Ingresar")
                            }
                        }

                        state.error?.let {
                            Spacer(Modifier.height(12.dp))
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .widthIn(min = 280.dp, max = 400.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(Modifier.height(16.dp))

                        Text("Bienvenido...!!!!", style = MaterialTheme.typography.titleLarge)

                        Spacer(Modifier.height(12.dp))

                        Text("Email: ${state.userProfile!!.email}", style = MaterialTheme.typography.bodyMedium)
                        Text("ID USER: ${state.userProfile!!.id}", style = MaterialTheme.typography.bodyMedium)

                        Spacer(Modifier.height(24.dp))

                        OutlinedButton(
                            onClick = viewModel::logout,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Cerrar sesión")
                        }
                    }
                }
            }
        }
    }
}
