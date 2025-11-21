package com.example.wherenow.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.wherenow.navigation.NavRoutes

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color(0xFFF7F6FB),   // fondo claro
        tonalElevation = 4.dp
    ) {
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
            label = { Text("Circles") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF7E57C2),
                selectedTextColor = Color(0xFF7E57C2),
                unselectedIconColor = Color(0xFF9E9E9E),
                unselectedTextColor = Color(0xFF9E9E9E),
                indicatorColor = Color(0xFFEDE7F6)
            )
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
            label = { Text("Search") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF7E57C2),
                selectedTextColor = Color(0xFF7E57C2),
                unselectedIconColor = Color(0xFF9E9E9E),
                unselectedTextColor = Color(0xFF9E9E9E),
                indicatorColor = Color(0xFFEDE7F6)
            )
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
            label = { Text("Events") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF7E57C2),
                selectedTextColor = Color(0xFF7E57C2),
                unselectedIconColor = Color(0xFF9E9E9E),
                unselectedTextColor = Color(0xFF9E9E9E),
                indicatorColor = Color(0xFFEDE7F6)
            )
        )
    }
}

