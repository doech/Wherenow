package com.example.wherenow

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun ConfirmationScreen(navController: NavController) {
    Button(onClick = { navController.navigate("confirmation") }) {
        Text("Start exploring")
    }
}
