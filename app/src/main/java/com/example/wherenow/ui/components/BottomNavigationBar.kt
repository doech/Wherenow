package com.example.wherenow.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.wherenow.navigation.NavRoutes

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == NavRoutes.HOME,
            onClick = {
                if (currentRoute != NavRoutes.HOME) {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Circles") }
        )
        NavigationBarItem(
            selected = currentRoute == NavRoutes.SEARCH,
            onClick = {
                if (currentRoute != NavRoutes.SEARCH) {
                    navController.navigate(NavRoutes.SEARCH) {
                        popUpTo(NavRoutes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            },
            icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            label = { Text("Search") }
        )
        NavigationBarItem(
            selected = currentRoute == NavRoutes.EVENTS,
            onClick = {
                if (currentRoute != NavRoutes.EVENTS) {
                    navController.navigate(NavRoutes.EVENTS) {
                        popUpTo(NavRoutes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            },
            icon = { Icon(Icons.Filled.Event, contentDescription = "Events") },
            label = { Text("Events") }
        )
    }
}
