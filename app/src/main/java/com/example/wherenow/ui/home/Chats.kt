package com.example.wherenow.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// -----------------------------------------------------------------------------
// 🧩 MODELOS DE DATOS
// -----------------------------------------------------------------------------
data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val senderName: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isOwnMessage: Boolean
)

data class ChatInfo(
    val circleName: String,
    val memberCount: Int,
    val circleColor: Color
)

// -----------------------------------------------------------------------------
// 💬 PANTALLA DE CHAT
// -----------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    circleName: String = "Music Lovers",
    memberCount: Int = 12,
    circleColor: Color = Color(0xFF9C27B0),
    modifier: Modifier = Modifier
) {
    // Estados
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(getMockMessages()) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Scroll automático al último mensaje
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Scaffold(
        topBar = {
            ChatTopBar(
                circleName = circleName,
                memberCount = memberCount,
                circleColor = circleColor,
                onBackClick = { navController.popBackStack() },
                onVideoCallClick = { /* TODO: Implementar videollamada */ },
                onCallClick = { /* TODO: Implementar llamada */ },
                onSettingsClick = { /* TODO: Implementar configuración */ }
            )
        },
        bottomBar = {
            ChatBottomBar(
                messageText = messageText,
                onMessageTextChange = { messageText = it },
                onSendClick = {
                    if (messageText.isNotBlank()) {
                        messages = messages + ChatMessage(
                            senderName = "You",
                            content = messageText,
                            isOwnMessage = true
                        )
                        messageText = ""
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { message ->
                    MessageBubble(message)
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 🔝 BARRA SUPERIOR DEL CHAT
// -----------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    circleName: String,
    memberCount: Int,
    circleColor: Color,
    onBackClick: () -> Unit,
    onVideoCallClick: () -> Unit,
    onCallClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                // Ícono del grupo
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(circleColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = circleColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = circleName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "$memberCount members",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        },
        actions = {
            IconButton(onClick = onVideoCallClick) {
                Icon(
                    imageVector = Icons.Default.VideoCall,
                    contentDescription = "Video Call",
                    tint = Color(0xFF9C27B0)
                )
            }
            IconButton(onClick = onCallClick) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call",
                    tint = Color(0xFF9C27B0)
                )
            }
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color(0xFF9C27B0)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

// -----------------------------------------------------------------------------
// 💬 BURBUJA DE MENSAJE
// -----------------------------------------------------------------------------
@Composable
fun MessageBubble(message: ChatMessage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isOwnMessage) Alignment.End else Alignment.Start
    ) {
        // Nombre del remitente (solo para mensajes de otros)
        if (!message.isOwnMessage) {
            Text(
                text = message.senderName,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 12.dp, bottom = 4.dp)
            )
        }

        // Burbuja del mensaje
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isOwnMessage) 16.dp else 4.dp,
                        bottomEnd = if (message.isOwnMessage) 4.dp else 16.dp
                    )
                )
                .background(
                    if (message.isOwnMessage) Color(0xFF9C27B0) else Color.White
                )
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.content,
                fontSize = 15.sp,
                color = if (message.isOwnMessage) Color.White else Color.Black,
                lineHeight = 20.sp
            )
        }

        // Timestamp
        Text(
            text = formatTimestamp(message.timestamp),
            fontSize = 11.sp,
            color = Color.Gray,
            modifier = Modifier.padding(
                start = if (message.isOwnMessage) 0.dp else 12.dp,
                end = if (message.isOwnMessage) 12.dp else 0.dp,
                top = 4.dp
            )
        )
    }
}

// -----------------------------------------------------------------------------
// ⬇️ BARRA INFERIOR DE INPUT
// -----------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBottomBar(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Campo de texto
            OutlinedTextField(
                value = messageText,
                onValueChange = onMessageTextChange,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp, max = 120.dp),
                placeholder = {
                    Text(
                        text = "Message Music Lovers NYC...",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedBorderColor = Color(0xFF9C27B0),
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp),
                maxLines = 4
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Botón de enviar
            IconButton(
                onClick = onSendClick,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF9C27B0)),
                enabled = messageText.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 🛠️ FUNCIONES AUXILIARES
// -----------------------------------------------------------------------------
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun getMockMessages(): List<ChatMessage> {
    val twoMinutesAgo = System.currentTimeMillis() - (2 * 60 * 1000)
    val oneMinuteAgo = System.currentTimeMillis() - (1 * 60 * 1000)
    val thirtySecondsAgo = System.currentTimeMillis() - (30 * 1000)

    return listOf(
        ChatMessage(
            senderName = "Sarah Chen",
            content = "Anyone going to the jazz festival this weekend?",
            timestamp = twoMinutesAgo,
            isOwnMessage = false
        ),
        ChatMessage(
            senderName = "You",
            content = "I'm definitely interested! What time does it start?",
            timestamp = oneMinuteAgo,
            isOwnMessage = true
        ),
        ChatMessage(
            senderName = "Mike Rodriguez",
            content = "It starts at 8 PM. I can pick up tickets for everyone if needed.",
            timestamp = thirtySecondsAgo,
            isOwnMessage = false
        )
    )
}