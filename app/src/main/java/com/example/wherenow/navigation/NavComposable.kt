package com.example.wherenow.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.wherenow.ui.auth.AuthScreen
import com.example.wherenow.ui.events.EventsScreen
import com.example.wherenow.ui.home.ChatScreen
import com.example.wherenow.ui.home.HomeScreen
import com.example.wherenow.ui.location.LocationScreen
import com.example.wherenow.ui.quiz.QuizScreen
import com.example.wherenow.ui.search.SearchScreen

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
        composable(NavRoutes.AUTH)    { AuthScreen(navController) }
        composable(NavRoutes.LOCATION){ LocationScreen(navController) }
        composable(NavRoutes.QUIZ)    { QuizScreen(navController) }
        composable(NavRoutes.HOME)    { HomeScreen(navController) }
        composable(NavRoutes.SEARCH)  { SearchScreen(navController) }
        composable(NavRoutes.EVENTS)  { EventsScreen(navController) }

        //  Chat con argumentos
        composable(
            route = "${NavRoutes.CHAT}/{name}/{members}/{color}",
            arguments = listOf(
                navArgument("name")    { type = NavType.StringType },
                navArgument("members") { type = NavType.IntType    },
                navArgument("color")   { type = NavType.IntType    } // ARGB Int
            )
        ) { backStackEntry ->
            val name    = backStackEntry.arguments?.getString("name") ?: "Circle"
            val members = backStackEntry.arguments?.getInt("members") ?: 0
            val colorInt= backStackEntry.arguments?.getInt("color") ?: 0xFF9C27B0.toInt()
            val color   = Color(colorInt)

            ChatScreen(
                navController = navController,
                circleName   = name,
                memberCount  = members,
                circleColor  = color
            )
        }
    }
}
