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
import androidx.compose.runtime.collectAsState
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
import com.example.wherenow.data.model.CircleRow
import com.example.wherenow.ui.search.SearchViewModel
import java.util.Date
import java.util.concurrent.TimeUnit

@Composable
fun HomeScreen(navController: NavController, searchViewModel: SearchViewModel = viewModel()) {

    val circles by searchViewModel.circles.collectAsState()

    LaunchedEffect(Unit) {
        searchViewModel.loadData()
    }

    var showDialog by remember { mutableStateOf(false) }
    var showEventAcceptedPopup by remember { mutableStateOf(false) }

    //LaunchedEffect(Unit) { showEventAcceptedPopup = true }

    val headerHeight = 112.dp

    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
        ) {
            AppHeader(
                userName = "Usuario",
                handle = "@Usuario123",
                onProfileClick = { navController.navigate("Pega_aquí_tu_ruta_de_perfil") }
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
                items(circles) { circle ->
                    CircleCard(circle) {
                        val encodedName = Uri.encode(circle.name)
                        val members = circle.membersCount
                        val colorArgb = Color(0xFF9C27B0).toArgb()

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
fun CircleCard(circle: CircleRow, onClick: () -> Unit) {

    val color = Color(0xFF9C27B0)

    val timeAgo = circle.lastActivity?.let { date ->
        val diff = Date().time - date.time
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        when {
            minutes < 60 -> "${minutes}m ago"
            minutes < 1440 -> "${minutes / 60}h ago"
            else -> "${minutes / 1440}d ago"
        }
    } ?: ""

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
                        .background(color)
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
                            text = if (circle.visibility == "public") "🔓 Public" else "🔒 Private",
                            fontSize = 12.sp,
                            color = if (circle.visibility == "public") Color(0xFF9C27B0) else Color.Gray
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${circle.membersCount} members  •  $timeAgo",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = circle.description,
                fontSize = 13.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
