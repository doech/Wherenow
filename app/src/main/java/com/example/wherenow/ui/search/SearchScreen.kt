package com.example.wherenow.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.wherenow.ui.components.BottomNavigationBar
import androidx.navigation.compose.rememberNavController
import com.example.wherenow.ui.components.AppHeader

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = viewModel(),
    onNavigateToEvent: (String) -> Unit = {},
    onNavigateToCircle: (String) -> Unit = {},
    onNavigateToProfile: (String) -> Unit = {}
) {
    val query by viewModel.query.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val headerHeight = 172.dp

    Scaffold(
        containerColor = Color(0xFFF7F6FB), // fondo claro como en Events
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF7F6FB))
        ) {
            // Header reutilizable (gradiente SOLO arriba)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
            ) {
                AppHeader(
                    userName = "Usuario",
                    handle = "@Usuario123",
                    onProfileClick = { /* navController.navigate("profile") si quieres */ }
                )
            }

            // Contenido bajo el header
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = headerHeight + 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                // Tarjeta de búsqueda (estilo tarjeta blanca con bordes redondeados)
                SearchCard(
                    query = query,
                    selectedFilter = selectedFilter,
                    onQueryChange = { viewModel.updateQuery(it) },
                    onFilterChange = { viewModel.updateFilter(it) },
                    hasResults = searchResults.isNotEmpty()
                )

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    isLoading -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }

                    searchResults.isEmpty() && query.isBlank() -> Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFFB0B0B0),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Start Searching",
                                color = Color(0xFF3F3F3F),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Find events, circles, and people that match your interests",
                                color = Color(0xFF7A7A7A),
                                fontSize = 13.sp
                            )
                        }
                    }

                    else -> SearchResultsList(
                        results = searchResults,
                        onResultClick = { result ->
                            when (result.type) {
                                "event" -> onNavigateToEvent(result.id)
                                "circle" -> onNavigateToCircle(result.id)
                                "user" -> onNavigateToProfile(result.id)
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun SearchCard(
    query: String,
    selectedFilter: String,
    onQueryChange: (String) -> Unit,
    onFilterChange: (String) -> Unit,
    hasResults: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search events, circles, or people...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7E57C2),
                    unfocusedBorderColor = Color(0xFFE3E3E3),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Events", "Circles", "People").forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { onFilterChange(filter) },
                        label = { Text(filter) },
                        shape = RoundedCornerShape(24.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF7E57C2),
                            selectedLabelColor = Color.White,
                            containerColor = Color(0xFFF4F4F6),
                            labelColor = Color(0xFF3F3F3F)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResultsList(results: List<SearchResult>, onResultClick: (SearchResult) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(results) { result ->
            SearchResultItem(result = result, onClick = { onResultClick(result) })
        }
    }
}

@Composable
fun SearchResultItem(result: SearchResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = when (result.type) {
                "event" -> Icons.Default.Event
                "circle" -> Icons.Default.Group
                "user" -> Icons.Default.Person
                else -> Icons.Default.Search
            }
            Icon(
                imageVector = icon,
                contentDescription = result.type,
                tint = Color(0xFF9C27B0),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = result.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = when (result.type) {
                        "event" -> "Event"
                        "circle" -> "Circle"
                        "user" -> "Person"
                        else -> ""
                    },
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    val navController = rememberNavController()
    MaterialTheme { SearchScreen(navController = navController) }
}