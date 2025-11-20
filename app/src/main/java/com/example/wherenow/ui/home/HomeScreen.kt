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
import com.example.wherenow.ui.components.BottomNavigationBar
import com.example.wherenow.ui.components.AppHeader
import android.net.Uri
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wherenow.navigation.NavRoutes
import com.example.wherenow.ui.auth.AuthViewModel
import androidx.compose.runtime.collectAsState

data class Circle(
    val name: String,
    val description: String,
    val members: Int,
    val timeAgo: String,
    val lastMessage: String,
    val isPublic: Boolean,
    val color: Color
)


@Composable
fun HomeScreen(navController: NavController) {
    val mockCircles = remember {
        listOf(
            Circle("Music Lovers Network","Discussing concerts and music",24,"2m ago","Anyone going to the jazz festival?",true, Color(0xFF9C27B0)),
            Circle("Foodie Adventures","Private group for food events",8,"1h ago","Found an amazing new...",false, Color(0xFFFF5722)),
            Circle("Art Gallery Walks","Weekly art gallery visits",15,"3h ago","Next gallery walk is this Satur...",true, Color(0xFFE91E63)),
            Circle("Tech Meetup Sessions","Private tech networking group",12,"1d ago","Great presentation at...",false, Color(0xFF2196F3))
        )
    }

    val authViewModel: AuthViewModel = viewModel()
    val currentUser by authViewModel.user.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showEventAcceptedPopup by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { showEventAcceptedPopup = true }

    val headerHeight = 172.dp

    Box(modifier = Modifier.fillMaxSize()) {
        // Header con gradiente (AppHeader)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
        ) {
            AppHeader(
                userName = currentUser?.name ?: "Usuario",
                handle = "@${currentUser?.username ?: "usuario"}",
                onProfileClick = { navController.navigate("profile") },
                onLogoutClick = {
                    authViewModel.logout()
                    navController.navigate(NavRoutes.AUTH) {
                        popUpTo(0)
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = headerHeight + 8.dp)
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
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
                ) { Text("+ New Circle") }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(mockCircles) { circle ->
                    CircleCard(circle) {
                        val encodedName = Uri.encode(circle.name)
                        val members = circle.members
                        val colorArgb = circle.color.toArgb()

                        navController.navigate("${NavRoutes.CHAT}/$encodedName/$members/$colorArgb")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            BottomNavigationBar(navController)
        }

        if (showDialog) {
            CreateCircle(
                onDismiss = { showDialog = false },
                onCreate = { _, _, _ -> showDialog = false }
            )
        }

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