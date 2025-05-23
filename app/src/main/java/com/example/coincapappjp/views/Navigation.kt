package com.example.coincapappjp.views

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.coincapappjp.navigation.BottomNavigationItem
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.coincapappjp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w(TAG, "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }
        val token = task.result
        val msg = "este es mi token----- "+token+" --"
        Log.d(TAG, msg)

    })
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomTabBar(navController) }
    ) {
        NavigationGraph(navController)
    }
}



@Composable
fun NavigationGraph(navController: NavHostController) {
    val assetIdKey = "assetId"
    NavHost(navController, startDestination = BottomNavigationItem.Home.route) {
        composable(BottomNavigationItem.Home.route) {
            AssetsList(navController = navController)
        }
        composable(BottomNavigationItem.Favourites.route) { FavouritesView(navController = navController) }
        composable(BottomNavigationItem.Settings.route) { SettingsView() }

        composable(
            route = "${BottomNavigationItem.Home.route}/{assetId}/{assetName}",
            arguments = listOf(
                navArgument("assetId") { type = NavType.StringType },
                navArgument("assetName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val assetId = backStackEntry.arguments?.getString("assetId") ?: "unknown"
            val assetName = backStackEntry.arguments?.getString("assetName") ?: "unknown"
            AssetDetailView(
                assetId = assetId,
                assetName = assetName,
                navController = navController
            )
        }

    }
}

@Composable
fun BottomTabBar(navController: NavHostController) {
    val items = listOf(
        BottomNavigationItem.Home,
        BottomNavigationItem.Favourites,
        BottomNavigationItem.Settings
    )

    BottomAppBar {
        val navBackStack by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStack?.destination?.route

        items.forEach { barItem ->
            val selected = barItem.route == currentRoute
            NavigationBarItem(
                selected = selected,
                label = { Text(barItem.title) },
                onClick = {
                    navController.navigate(barItem.route) {
                        navController.graph.startDestinationRoute.let { route ->
                            if ( route != null ) {
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) barItem.selectedIcon else barItem.unselectedIcon,
                        contentDescription = null
                    )
                }
            )
        }
    }
}