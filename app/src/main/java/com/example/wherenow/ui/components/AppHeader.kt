package com.example.wherenow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout


@Composable
fun AppHeader(
    userName: String = "Usuario",
    handle: String = "@Usuario123",
    onProfileClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    // Gradiente SOLO arriba
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF9C8FFF), Color(0xFFF871FF)),
        startY = 0f,
        endY = 600f
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(172.dp)
            .background(brush = gradient, shape = RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp))
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0))
                    .clickable { onProfileClick() }
            )
            Column {
                Text(userName, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Text(handle, color = Color(0xFFEDE7F6), fontSize = 13.sp)
            }

            IconButton(
                onClick = onLogoutClick
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Cerrar sesión",
                    tint = Color.White
                )
            }
        }
    }
}
