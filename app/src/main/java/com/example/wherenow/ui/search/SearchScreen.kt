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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.wherenow.ui.components.BottomNavigationBar
import com.example.wherenow.ui.components.AppHeader
import com.example.wherenow.navigation.NavRoutes
import com.example.wherenow.data.model.SearchResult

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = viewModel()
) {
    // carga inicial
    LaunchedEffect(Unit) { viewModel.loadData() }

    val users by viewModel.users.collectAsState()
    val events by viewModel.events.collectAsState()
    val circles by viewModel.circles.collectAsState()

    // estado local (ANTES estaba en el ViewModel)
    var query by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var isLoading by remember { mutableStateOf(true) }

    // simula loading inicial
    LaunchedEffect(users, events, circles) {
        if (users.isNotEmpty() || events.isNotEmpty() || circles.isNotEmpty()) {
            isLoading = false
        }
    }

    // construir lista combinada + filtrar
    val searchResults = remember(query, selectedFilter, users, events, circles) {
        val all = mutableListOf<SearchResult>()

        all += users.map {
            SearchResult(id = it.id, name = it.name, type = "user")
        }
        all += events.map {
            SearchResult(id = it.eventId, name = it.name, type = "event")
        }
        all += circles.map {
            SearchResult(id = it.id, name = it.name, type = "circle")
        }

        // filtro de tipo
        val filtered = when (selectedFilter) {
            "Events" -> all.filter { it.type == "event" }
            "Circles" -> all.filter { it.type == "circle" }
            "People" -> all.filter { it.type == "user" }
            else -> all
        }

        // filtro por texto
        filtered.filter { it.name.contains(query, ignoreCase = true) }
    }

    val headerHeight = 172.dp

    Scaffold(
        containerColor = Color(0xFFF7F6FB),
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
            ) {
                AppHeader(
                    userName = "Usuario",
                    handle = "@Usuario123",
                    onProfileClick = {}
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = headerHeight + 16.dp, start = 16.dp, end = 16.dp)
            ) {
                SearchCard(
                    query = query,
                    selectedFilter = selectedFilter,
                    onQueryChange = { query = it },
                    onFilterChange = { selectedFilter = it },
                    hasResults = searchResults.isNotEmpty()
                )

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    isLoading -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }

                    searchResults.isEmpty() && query.isBlank() -> EmptySearchState()

                    searchResults.isEmpty() -> NoResultsState()

                    else -> SearchResultsList(
                        results = searchResults,
                        onResultClick = { result ->
                            when (result.type) {
                                "event" -> navController.navigate(NavRoutes.EVENTS)
                                "circle" -> navController.navigate(NavRoutes.HOME)
                                "user" -> navController.navigate(NavRoutes.HOME)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptySearchState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFB0B0B0), modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text("Start Searching", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Find events, circles, and people", fontSize = 13.sp, color = Color.Gray)
        }
    }
}

@Composable
fun NoResultsState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No results found", color = Color.Gray)
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
                shape = RoundedCornerShape(12.dp)
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
                        shape = RoundedCornerShape(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResultsList(results: List<SearchResult>, onResultClick: (SearchResult) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(results) { result ->
            SearchResultItem(result, onClick = { onResultClick(result) })
        }
    }
}

@Composable
fun SearchResultItem(result: SearchResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = when (result.type) {
                "event" -> Icons.Default.Event
                "circle" -> Icons.Default.Group
                "user" -> Icons.Default.Person
                else -> Icons.Default.Search
            }
            Icon(icon, contentDescription = null, tint = Color(0xFF9C27B0), modifier = Modifier.size(32.dp))

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(result.name, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text(result.type.replaceFirstChar { it.uppercase() }, fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}
