package com.example.coincapappjp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItem(
    val route: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val title: String
) {
    data object Home: BottomNavigationItem(
        route = "home",
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        title = "Home"
    )

    data object Favourites: BottomNavigationItem(
        route = "favourites",
        unselectedIcon = Icons.Outlined.Star,
        selectedIcon = Icons.Filled.Star,
        title = "Favourites"
    )

    data object Settings: BottomNavigationItem(
        route = "settings",
        unselectedIcon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings,
        title = "Settings"
    )
}