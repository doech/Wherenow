package com.example.wherenow.ui.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// -----------------------------------------------------------------------------
// 🧩 MODELO LOCAL DE DATOS
// -----------------------------------------------------------------------------
data class Circle(
    val name: String,
    val description: String,
    val members: Int,
    val timeAgo: String,
    val lastMessage: String,
    val isPublic: Boolean,
    val color: Color
)

// -----------------------------------------------------------------------------
// 🏠 HOME SCREEN
// -----------------------------------------------------------------------------
@Composable
fun HomeScreen(navController: NavController) {
    val mockCircles = remember {
        listOf(
            Circle(
                name = "Music Lovers Network",
                description = "Discussing concerts and music",
                members = 24,
                timeAgo = "2m ago",
                lastMessage = "Anyone going to the jazz festival?",
                isPublic = true,
                color = Color(0xFF9C27B0)
            ),
            Circle(
                name = "Foodie Adventures",
                description = "Private group for food events",
                members = 8,
                timeAgo = "1h ago",
                lastMessage = "Found an amazing new...",
                isPublic = false,
                color = Color(0xFFFF5722)
            ),
            Circle(
                name = "Art Gallery Walks",
                description = "Weekly art gallery visits",
                members = 15,
                timeAgo = "3h ago",
                lastMessage = "Next gallery walk is this Satur...",
                isPublic = true,
                color = Color(0xFFE91E63)
            ),
            Circle(
                name = "Tech Meetup Sessions",
                description = "Private tech networking group",
                members = 12,
                timeAgo = "1d ago",
                lastMessage = "Great presentation at...",
                isPublic = false,
                color = Color(0xFF2196F3)
            )
        )
    }

    // 🔔 estado para abrir/cerrar el diálogo de creación
    var showDialog by remember { mutableStateOf(false) }

    // 🎉 estado para mostrar el popup de evento aceptado
    var showEventAcceptedPopup by remember { mutableStateOf(false) }

    // 🚀 NUEVO: Mostrar popup automáticamente al entrar
    LaunchedEffect(Unit) {
        delay(500) // espera medio segundo para que la pantalla cargue
        showEventAcceptedPopup = true
    }

    // -------------------------------------------------------------------------
    // 🔝 ESTRUCTURA PRINCIPAL
    // -------------------------------------------------------------------------
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // 🔔 Iconos de notificaciones y ajustes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { navController.navigate("Pega_aquí_tu_ruta_de_notificaciones") }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                }
                IconButton(onClick = { navController.navigate("Pega_aquí_tu_ruta_de_configuración") }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            }

            // 👤 Encabezado del usuario
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFCCCCCC))
                        .clickable { navController.navigate("Pega_aquí_tu_ruta_de_perfil") }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Usuario", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("@Usuario123", color = Color.Gray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Sección "Your Circles" con botón +New Circle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Your Circles", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("+ New Circle")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 🟣 Lista de círculos
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(mockCircles) { circle ->
                    CircleCard(circle) {
                        navController.navigate("Pega_aquí_tu_ruta_para_circleDetail/${circle.name}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ⬇️ Barra inferior de navegación
            BottomNavigationBar(navController)
        }

        // -------------------------------------------------------------------------
        // 🪟 Diálogo emergente para crear círculo (popup)
        // -------------------------------------------------------------------------
        if (showDialog) {
            CreateCircle(
                onDismiss = { showDialog = false },
                onCreate = { name, desc, isPrivate ->
                    println("Circle created: $name | $desc | Private? $isPrivate")
                    showDialog = false
                }
            )
        }

        // -------------------------------------------------------------------------
        // 🎉 Popup de evento aceptado - APARECE AUTOMÁTICAMENTE
        // -------------------------------------------------------------------------
        if (showEventAcceptedPopup) {
            EventAcceptedPopup(
                eventInfo = EventInfo(
                    name = "Food Truck Festival",
                    description = "Over 20 gourmet food trucks featuring cuisines from around the world",
                    hasPlusOnePrivilege = true
                ),
                onDismiss = { showEventAcceptedPopup = false }
            )
        }
    }
}

// -----------------------------------------------------------------------------
// 🃏 TARJETA DE CADA CÍRCULO
// -----------------------------------------------------------------------------
@Composable
fun CircleCard(circle: Circle, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(circle.color)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = circle.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = circle.description,
                            color = Color.Gray,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (circle.isPublic) "🔓 Public" else "🔒 Private",
                            fontSize = 12.sp,
                            color = if (circle.isPublic) Color(0xFF9C27B0) else Color.Gray
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${circle.members} members  •  ${circle.timeAgo}",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = circle.lastMessage,
                fontSize = 13.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// -----------------------------------------------------------------------------
// ⚓️ BARRA DE NAVEGACIÓN INFERIOR
// -----------------------------------------------------------------------------
@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = { navController.navigate("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Circles") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("search") },
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") }
        )
    }
}