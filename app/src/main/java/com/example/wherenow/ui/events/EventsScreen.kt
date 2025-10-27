package com.example.wherenow.ui.events


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wherenow.ui.home.BottomNavigationBar

// MOD de datos
data class Event(
    val title: String,
    val category: String,
    val description: String,
    val location: String,
    val time: String,
    val price: String,
    val interested: Int,
    val distance: String,
    val mutualFriends: List<Friend>
)

data class Friend(
    val name: String,
    val mutualFriends: Int
)

@Composable
fun EventsScreen(navController: NavController) {
    // Pantalla vacía
    //emily y ale s

    val context = LocalContext.current

    // Estado: evento seleccionado (null = vista de lista)
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    // HARD-CODED DATA
    val events = listOf(
        Event(
            title = "Food Truck Festival",
            category = "Food",
            description = "Over 20 gourmet food trucks featuring cuisines from around the world",
            location = "Brooklyn Bridge Park",
            time = "Tomorrow, 12:00 PM",
            price = "$15 entry",
            interested = 89,
            distance = "1.2 miles away",
            mutualFriends = listOf(
                Friend("David ", 5),
                Friend("Lisa ", 1)
            )
        ),
        Event(
            title = "Local Farmers Market",
            category = "Food",
            description = "Fresh produce and local delicacies from community farmers.",
            location = "Central Park",
            time = "Saturday, 9:00 AM",
            price = "Free entry",
            interested = 142,
            distance = "0.8 miles away",
            mutualFriends = listOf(Friend("Maria Lopez", 3))
        ),
        Event(
            title = "Wine & Cheese Night",
            category = "Food",
            description = "Sample exclusive wines paired with gourmet cheeses.",
            location = "The Wine Cellar",
            time = "Friday, 7:00 PM",
            price = "$25 entry",
            interested = 56,
            distance = "2.1 miles away",
            mutualFriends = emptyList()
        )
    )


    //BG gradient
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF9C8FFF),
            Color(0xFFF871FF),
            Color(0xFFFA8355)
        )
    )

    Scaffold(
        bottomBar = {
            // Barra de nav
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush)
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {

            // HEADER from HomeScreen + "Create Event" button
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Top-right icons (notifications + settings)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { navController.navigate("notifications") }) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }

                // User info row (profile picture + username)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFFCCCCCC))
                                .clickable { navController.navigate("profile") } // 🆕 opens profile
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Usuario", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("@Usuario123", color = Color.DarkGray, fontSize = 14.sp)
                        }
                    }

                    // Create Event button
                    Button(
                        onClick = {
                            Toast.makeText(context, "Create Event clicked", Toast.LENGTH_SHORT)
                                .show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB97A56)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Event")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Create Event")
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Contenido principal
            Box(modifier = Modifier.weight(1f)) {
                if (selectedEvent == null) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(events) { event ->
                            EventCard(
                                event = event,
                                onViewDetails = { selectedEvent = event }
                            )
                        }
                    }
                } else {
                    EventDetails(event = selectedEvent!!, onBack = { selectedEvent = null })
                }
            }
        }
    }
}

@Composable
fun EventCard(event: Event, onViewDetails: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onViewDetails() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Categoría
            AssistChip(
                onClick = {},
                label = { Text(event.category) },
                colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFFEDE7F6))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Título y descripción
            Text(event.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(event.description, color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Ubicación y hora
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Place, contentDescription = "Location", tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(event.location)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.AccessTime, contentDescription = "Time", tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(event.time)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // "View Details"
            Button(
                onClick = onViewDetails,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Info, contentDescription = "Details")
                Spacer(modifier = Modifier.width(4.dp))
                Text("View Details")
            }
        }
    }
}

@Composable
fun EventDetails(event: Event, onBack: () -> Unit) {
    val context = LocalContext.current

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Back
            TextButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Back")
            }

            // Detalles del evento
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    AssistChip(
                        onClick = {},
                        label = { Text(event.category) },
                        colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFFEDE7F6))
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(event.title, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Text(event.description, color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Fecha, precio y asistentes
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.AccessTime,
                                    contentDescription = "Time",
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(event.time)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Place,
                                    contentDescription = "Place",
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(event.location)
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.AttachMoney,
                                    contentDescription = "Price",
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(event.price, fontWeight = FontWeight.Bold)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.People,
                                    contentDescription = "Interested",
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("${event.interested} interested")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("People you may know", fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Lista de amigos
                    for (friend in event.mutualFriends) {
                        FriendRow(friend)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón de solicitud
                    Button(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Request sent to join event",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Filled.EventAvailable, contentDescription = "Request")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Request Access to Event")
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Event organizer will review your request",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                }
            }
        }
    }
}

@Composable
fun FriendRow(friend: Friend) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(friend.name, fontWeight = FontWeight.Bold)
                Text(
                    "${friend.mutualFriends} mutual friends",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Button(onClick = { /* futuro chat */ }, shape = RoundedCornerShape(8.dp)) {
                Icon(Icons.Filled.Message, contentDescription = "Message")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Message")
            }
        }
    }
}
