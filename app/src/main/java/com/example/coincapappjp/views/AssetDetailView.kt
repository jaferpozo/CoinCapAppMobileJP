// com/example/coincapappjp/views/AssetDetailView.kt
package com.example.coincapappjp.views

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.coincapappjp.viewModels.AssetDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetDetailView(
    assetId: String,
    assetName: String,
    navController: NavHostController
) {
    val viewModel: AssetDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.addResult) {
        uiState.addResult?.let { result ->
            if (result.isSuccess) {
                Toast.makeText(
                    context,
                    "✓ $assetName agregado a Favoritos",
                    Toast.LENGTH_SHORT
                ).show()
                navController.popBackStack() // Volver a Home
            } else {
                Toast.makeText(
                    context,
                    "✗ ${result.exceptionOrNull()?.message ?: "Error desconocido"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            viewModel.clearAddResult()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Detalle") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (uiState.loading) {
                CircularProgressIndicator()
            } else if (uiState.error != null) {
                Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
            } else {
                uiState.asset?.let {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(Modifier.height(8.dp))
                            Text("ID: ${it.id}", style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(24.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.addToFavorites() },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Agregar")
                                }
                                OutlinedButton(
                                    onClick = { navController.popBackStack() },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("No agregar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
