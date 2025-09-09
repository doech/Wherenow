package com.example.wherenow

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun NavComposable(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.AUTH,
        modifier = modifier
    ) {
        composable(NavRoutes.AUTH)        { AuthScreen(navController) }
        composable(NavRoutes.LOCATION)     { LocationScreen(navController) }
        composable(NavRoutes.QUIZ)         { QuizScreen(navController) }
        composable(NavRoutes.CONFIRMATION) { ConfirmationScreen(navController) }
    }
}
