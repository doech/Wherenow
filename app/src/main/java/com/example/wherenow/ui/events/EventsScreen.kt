package com.example.wherenow.ui.events

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wherenow.data.model.EventRow
import com.example.wherenow.data.repository.FirestoreEventRepository
import com.example.wherenow.ui.components.AppHeader
import com.example.wherenow.ui.components.BottomNavigationBar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.LayoutDirection

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wherenow.ui.auth.AuthViewModel
import com.example.wherenow.navigation.NavRoutes

@Composable
fun EventsScreen(navController: NavController) {
    val repo = remember { FirestoreEventRepository() }
    val eventsViewModel: EventsViewModel = viewModel()

    val authViewModel: AuthViewModel = viewModel()
    val currentUserId = authViewModel.user.collectAsState().value?.id

    var events by remember { mutableStateOf<List<EventRow>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var selectedEvent by remember { mutableStateOf<com.example.wherenow.data.model.EventRow?>(null) }

    val pendingId = navController
        .previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("selectedEventId")

    LaunchedEffect(pendingId) {
        if (!pendingId.isNullOrEmpty()) {
            try {
                val repo = com.example.wherenow.data.repository.FirestoreEventRepository()

                selectedEvent = repo.getById(pendingId)
            } catch (e: Exception) {
                error = e.message
            } finally {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.remove<String>("selectedEventId")
            }
        }
    }

    // Cargar eventos de Firestore
    LaunchedEffect(Unit) {
        try {
            events = repo.getAll()
            loading = false
        } catch (e: Exception) {
            error = e.message
            loading = false
        }
    }


    val headerHeight = 112.dp

    Scaffold(
        containerColor = Color(0xFFF7F6FB),
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .background(Color(0xFFF7F6FB))
        ) {
            // HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
            ) {
                AppHeader(
                    userName = "Usuario",
                    handle = "@Usuario123",
                    onProfileClick = { navController.navigate("profile") },
                    onLogoutClick = { authViewModel.logout()
                        navController.navigate(NavRoutes.AUTH) {
                            popUpTo(0)  // Limpia el backstack
                        }
                    }
                )
            }

            when {
                loading -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = headerHeight),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                error != null -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = headerHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: $error", color = Color.Red)
                }

                selectedEvent == null -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        contentPadding = PaddingValues(
                            top = headerHeight + 16.dp,
                            start = 16.dp, end = 16.dp, bottom = 16.dp
                        )
                    ) {
                        items(events) { event ->
                            EventCard(
                                event = event,
                                onViewDetails = { selectedEvent = event }
                            )
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = headerHeight)
                            .padding(horizontal = 16.dp)
                    ) {
                        EventDetails(
                            event = selectedEvent!!,
                            onBack = { selectedEvent = null }
                        )
                    }
                }
            }
        }
    }
}

// =====================================================
// CARD
// =====================================================
private fun formatEventDateShort(date: java.util.Date?): String =
    date?.let {
        java.text.SimpleDateFormat("EEE, h:mm a", java.util.Locale.getDefault()).format(it)
    } ?: "Date not available"

@Composable
fun EventCard(event: EventRow, onViewDetails: () -> Unit) {
    val timeText = formatEventDateShort(event.startAt ?: event.createdAt)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(22.dp))
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
                .clickable { onViewDetails() },        // toda la tarjeta abre detalles
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F6FB))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // chip de estado
                TagChip(event.status)

                Spacer(Modifier.height(10.dp))

                // SOLO nombre
                Text(
                    text = event.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color(0xFF262626)
                )

                Spacer(Modifier.height(8.dp))

                // SOLO hora y lugar
                InfoRow(Icons.Filled.AccessTime, timeText)
                Spacer(Modifier.height(6.dp))
                InfoRow(Icons.Filled.Place, event.location)

                Spacer(Modifier.height(14.dp))

                // Botón "Ver detalles"
                Button(
                    onClick = onViewDetails,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Ver detalles")
                }
            }
        }
    }
}


// =====================================================
// DETALLES
// =====================================================
@Composable
fun EventDetails(event: EventRow, onBack: () -> Unit) {

    val context = LocalContext.current

    val authViewModel: AuthViewModel = viewModel()
    val currentUserId = authViewModel.user.collectAsState().value?.id
    val eventsViewModel: EventsViewModel = viewModel()

    // Se recuerda por evento (clave = event.eventId) para que al cambiar de evento se resetee.
    var requestSent by remember(event.eventId) { mutableStateOf(false) }

    val dateFull = event.startAt?.let {
        java.text.SimpleDateFormat("EEEE, MMM d • h:mm a", java.util.Locale.getDefault()).format(it)
    } ?: "No date"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F4F8))
    ) {
        item {
            TextButton(onClick = onBack, modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                Spacer(Modifier.width(6.dp))
                Text("Back")
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    TagChip(event.status)
                    Spacer(Modifier.height(10.dp))

                    Text(event.name, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, color = Color(0xFF222222))
                    Spacer(Modifier.height(6.dp))

                    if (event.description.isNotBlank()) {
                        Text(event.description, color = Color(0xFF6F6F6F))
                        Spacer(Modifier.height(12.dp))
                    }

                    InfoRow(Icons.Filled.AccessTime, dateFull)
                    Spacer(Modifier.height(6.dp))
                    InfoRow(Icons.Filled.Place, event.location)
                    Spacer(Modifier.height(6.dp))
                    InfoRow(Icons.Filled.AttachMoney, "${event.interested} USD")
                    Spacer(Modifier.height(6.dp))
                    InfoRow(Icons.Filled.People, "${event.interested} interested")
                    if (event.distanceText.isNotBlank()) {
                        Spacer(Modifier.height(6.dp))
                        InfoRow(Icons.Filled.LocationSearching, event.distanceText)
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (!requestSent) {
                                requestSent = true

                                eventsViewModel.requestJoinEvent(event.eventId, currentUserId ?: "")

                                // Mostrar confirmación
                                Toast.makeText(
                                    context,
                                    "Request submitted",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !requestSent,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (requestSent) Color(0xFFBDBDBD) else Color(0xFF7E57C2)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Icon(Icons.Filled.EventAvailable, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text(if (requestSent) "Request Sent" else "Request Access to Event")
                    }

                    Spacer(Modifier.height(6.dp))
                    Text(
                        if (requestSent) "Request submitted ✓" else "Event organizer will review your request",
                        fontSize = 12.sp,
                        color = if (requestSent) Color(0xFF2E7D32) else Color(0xFF8C8C8C)
                    )
                }
            }
        }
    }
}


// =====================================================
// HELPERS
// =====================================================
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
