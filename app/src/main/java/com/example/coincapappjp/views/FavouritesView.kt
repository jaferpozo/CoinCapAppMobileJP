package com.example.coincapappjp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.coincapappjp.models.Asset
import com.example.coincapappjp.viewModels.FavouritesViewModel
import com.example.coincapappjp.viewModels.SettingsViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FavouritesView(
    viewModel: FavouritesViewModel = hiltViewModel(),
    navController: NavHostController,

) {
    val showNoSessionDialog by viewModel._showNoSessionDialog.collectAsState()
    val favourites by viewModel.favourites.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
        if (showNoSessionDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.clearNoSessionDialog() },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.clearNoSessionDialog()
                        navController.popBackStack()
                    }) {
                        Text("OK")
                    }
                },
                title = { Text("Sesión no iniciada") },
                text = { Text("Por favor inicia sesión para ver tus favoritos.") }
            )
        }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading && favourites.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (favourites.isEmpty()) {
            Text(
                text = "No hay favoritos aún",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = isLoading),
                onRefresh = { viewModel.fetchFavourites() }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.onBackground)
                ) {
                    items(favourites) { asset ->
                        AssetListItem(asset = asset)
                    }
                }
            }
        }
    }
}


@Composable
fun AssetListItem(asset: Asset) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            if (LocalInspectionMode.current) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(30.dp)
                )
            } else {
                AsyncImage(
                    model = "https://assets.coincap.io/assets/icons/${asset.symbol.lowercase()}@2x.png",
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }

        Column {
            Text(
                text = asset.symbol,
                fontSize = 18.sp,
                color = Color.White
            )
            Text(
                text = asset.name,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "${asset.percentage}%",
            modifier = Modifier.padding(horizontal = 16.dp),
            color = if (asset.percentage >= 0) Color.Green else Color.Red,
            style = typography.labelLarge

        )

        Text(
            text = "$${asset.price}",
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.White,
            style = typography.labelLarge
        )
    }
}

