package com.example.wherenow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ConfirmationScreen(navController: NavController) {
    // Lista provisional de intereses
    val interests = listOf("Interest 1", "Interest 2", "Interest 3")

    // Fondo degradado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFB388FF),
                        Color(0xFFFF80AB)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Tarjeta blanca en el centro
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White) // 👈 blanco
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Confirm your choices",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "You've selected ${interests.size} categories",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                interests.forEach { label ->
                    InterestChip(label)
                    Spacer(Modifier.height(8.dp))
                }

                Spacer(Modifier.height(24.dp))

                // Caja gris "What happens next?" (placeholder)
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("What happens next?", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        Text("• Placeholder text 1")
                        Text("• Placeholder text 2")
                        Text("• Placeholder text 3")
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Botones (uno de ejemplo)
                Button(
                    onClick = { navController.navigate(NavRoutes.LOGIN) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
                ) {
                    Text("Start Exploring →")
                }
            }
        }
    }
}

@Composable
fun InterestChip(label: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color(0xFFEDE7F6),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = Color(0xFF512DA8)
        )
    }
}
