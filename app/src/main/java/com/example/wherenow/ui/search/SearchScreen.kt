package com.example.wherenow.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun SearchScreen(
    navController: NavHostController? = null,
    viewModel: SearchViewModel = viewModel(),
    onNavigateToEvent: (String) -> Unit = {},
    onNavigateToCircle: (String) -> Unit = {},
    onNavigateToProfile: (String) -> Unit = {},
    onNavigateToCircles: () -> Unit = {},
    onNavigateToEvents: () -> Unit = {}
)
{
    val query by viewModel.query.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = "Search",
                onNavigateToCircles = onNavigateToCircles,
                onNavigateToEvents = onNavigateToEvents
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF9C27B0), // Morado
                            Color(0xFFE91E63)  // Rosa
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                SearchHeader()
                Spacer(modifier = Modifier.height(24.dp))
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
                    ) { CircularProgressIndicator(color = Color.White) }

                    searchResults.isEmpty() && query.isBlank() -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Start Searching",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
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
fun SearchHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User",
                    tint = Color(0xFF9C27B0)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "@Usuario123",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Row {
            Box {
                IconButton(onClick = { /* TODO: Abrir notificaciones */ }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                        .align(Alignment.TopEnd)
                        .offset(x = (-8).dp, y = 8.dp)
                )
            }
            IconButton(onClick = { /* TODO: Abrir configuración */ }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.White
                )
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF9C27B0),
                    unfocusedBorderColor = Color.LightGray
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
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF9C27B0),
                            selectedLabelColor = Color.White
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

@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onNavigateToCircles: () -> Unit,
    onNavigateToEvents: () -> Unit
) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Group, contentDescription = "Circles") },
            label = { Text("Circles") },
            selected = selectedTab == "Circles",
            onClick = onNavigateToCircles
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") },
            selected = selectedTab == "Search",
            onClick = { /* Ya estamos aquí */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Event, contentDescription = "Events") },
            label = { Text("Events") },
            selected = selectedTab == "Events",
            onClick = onNavigateToEvents
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    MaterialTheme { SearchScreen() }
}
