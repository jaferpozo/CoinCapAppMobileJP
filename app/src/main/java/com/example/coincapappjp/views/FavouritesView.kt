package com.example.coincapappjp.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.coincapappjp.models.Asset
import com.example.coincapappjp.viewModels.FavouritesViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun FavouritesView(
    viewModel: FavouritesViewModel = hiltViewModel()
) {
    val favourites by viewModel.favourites.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading && favourites.isEmpty()) {
            // Mostrar loading inicial
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (favourites.isEmpty()) {
            Text(
                text = "No hay favoritos aún",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            // Mostrar favoritos con SwipeRefresh
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = isLoading),
                onRefresh = { viewModel.fetchFavourites() }
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = asset.name, style = MaterialTheme.typography.titleMedium)
        Text(text = "${asset.symbol} - \$${asset.price}")
        Text(
            text = "${asset.percentage}%",
            color = if (asset.percentage >= 0) Color.Green else Color.Red
        )
    }
}

