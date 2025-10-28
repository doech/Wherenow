package com.example.wherenow.ui.events

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wherenow.ui.components.BottomNavigationBar
import com.example.wherenow.ui.components.AppHeader

// ===== DATA =====
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
    val context = LocalContext.current
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

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
            mutualFriends = listOf(Friend("David ", 5), Friend("Lisa ", 1))
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

    // Altura visual del header (para el padding del contenido)
    val headerHeight = 172.dp

    Scaffold(
        containerColor = Color(0xFFF7F6FB),
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF7F6FB))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
            ) {
                AppHeader(
                    userName = "Usuario",
                    handle = "@Usuario123",
                    onProfileClick = { navController.navigate("profile") }
                )
            }

            // CONTENIDO
            if (selectedEvent == null) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    contentPadding = PaddingValues(
                        top = headerHeight + 16.dp,
                        start = 16.dp, end = 16.dp, bottom = 16.dp
                    )
                ) {
                    items(events) { event ->
                        EventCard(event = event, onViewDetails = { selectedEvent = event })
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = headerHeight)
                        .padding(horizontal = 16.dp)
                ) {
                    EventDetails(event = selectedEvent!!, onBack = { selectedEvent = null })
                }
            }
        }
    }
}

/* ---------- UI helpers ---------- */

@Composable
private fun TagChip(text: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF3F0FA),
        border = BorderStroke(1.dp, Color(0xFFE2DDF6))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            fontSize = 12.sp,
            color = Color(0xFF6D5BBE),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color(0xFF8B8B8B), modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(label, fontSize = 14.sp, color = Color(0xFF4C4C4C))
    }
}

// ===== CARD =====
@Composable
fun EventCard(event: Event, onViewDetails: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(22.dp), clip = false)
            .background(
                brush = Brush.verticalGradient(listOf(Color(0xFFA78BFA), Color(0xFFF0ABFC))),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(1.5.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .clickable { onViewDetails() },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F6FB)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TagChip(event.category)
                Spacer(Modifier.height(10.dp))
                Text(event.title, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, color = Color(0xFF262626))
                Spacer(Modifier.height(4.dp))
                Text(event.description, color = Color(0xFF7C7C7C), fontSize = 13.sp, lineHeight = 18.sp)
                Spacer(Modifier.height(12.dp))
                InfoRow(Icons.Filled.Place, event.location)
                Spacer(Modifier.height(6.dp))
                InfoRow(Icons.Filled.AccessTime, event.time)
                Spacer(Modifier.height(14.dp))
                Button(
                    onClick = onViewDetails,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6246B5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Text("View Details", fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

// ===== DETAILS =====
@Composable
fun EventDetails(event: Event, onBack: () -> Unit) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F4F8))
            .padding(horizontal = 0.dp)
    ) {
        item {
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onBack, modifier = Modifier.padding(horizontal = 16.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                Spacer(Modifier.width(6.dp))
                Text("Back")
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    TagChip(event.category)
                    Spacer(Modifier.height(10.dp))
                    Text(event.title, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, color = Color(0xFF222222))
                    Spacer(Modifier.height(4.dp))
                    Text(event.description, color = Color(0xFF6F6F6F))
                    Spacer(Modifier.height(14.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            InfoRow(Icons.Filled.AccessTime, event.time)
                            InfoRow(Icons.Filled.Place, event.location)
                        }
                        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.AttachMoney, contentDescription = null, tint = Color(0xFF8B8B8B), modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(event.price, fontWeight = FontWeight.SemiBold, color = Color(0xFF2E2E2E))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.People, contentDescription = null, tint = Color(0xFF8B8B8B), modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("${event.interested} interested", color = Color(0xFF2E2E2E))
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Text("People you may know", fontWeight = FontWeight.Medium, color = Color(0xFF2E2E2E))
                    Spacer(Modifier.height(8.dp))

                    event.mutualFriends.forEach { FriendRow(it) }

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            Toast.makeText(context, "Request sent to join event", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Icon(Icons.Filled.EventAvailable, contentDescription = "Request")
                        Spacer(Modifier.width(6.dp))
                        Text("Request Access to Event", fontWeight = FontWeight.Medium)
                    }

                    Spacer(Modifier.height(6.dp))
                    Text("Event organizer will review your request", fontSize = 12.sp, color = Color(0xFF8C8C8C))
                }
            }
        }
    }
}

// ===== FRIEND ROW =====
@Composable
fun FriendRow(friend: Friend) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F8FC)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0))
                )
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(friend.name, fontWeight = FontWeight.SemiBold, color = Color(0xFF2E2E2E))
                    Text("${friend.mutualFriends} mutual friends", fontSize = 12.sp, color = Color(0xFF8C8C8C))
                }
            }
            OutlinedButton(
                onClick = { /* futuro chat */ },
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Filled.Message, contentDescription = "Message", modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Message")
            }
        }
    }
}
