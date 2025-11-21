package com.example.wherenow.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wherenow.data.model.JoinRequest
import com.example.wherenow.ui.events.EventsViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.wherenow.ui.auth.AuthViewModel

@Composable
fun JoinRequestDialog(
    request: JoinRequest,
    onDismiss: () -> Unit
) {
    val eventsViewModel: EventsViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = "Join Request",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text("User ${request.userName} wants to join your event:")
                Spacer(Modifier.height(10.dp))
                Text(
                    request.eventName,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF7E57C2)
                )
                Spacer(Modifier.height(12.dp))

                val formattedDate = remember(request.requestedAt) {
                    java.text.SimpleDateFormat(
                        "dd MMM yyyy • hh:mm a",
                        java.util.Locale.getDefault()
                    ).format(java.util.Date(request.requestedAt))
                }

                Text("Requested at: $formattedDate", color = Color.Gray)
            }
        },
        confirmButton = {
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF4CAF50)),
                onClick = {

                    eventsViewModel.acceptJoinRequest(
                        eventId = request.eventId,
                        userId = request.userId
                    )
                    authViewModel.removeRequest(
                        userId = request.userId,
                        eventId = request.eventId
                    )
                    onDismiss()
                }
            ) {
                Text("Accept")
            }
        },
        dismissButton = {
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFD32F2F)),
                onClick = {
                    eventsViewModel.rejectJoinRequest(
                        eventId = request.eventId,
                        userId = request.userId
                    )
                    authViewModel.removeRequest(
                        userId = request.userId,
                        eventId = request.eventId
                    )
                    onDismiss()
                }
            ) {
                Text("Reject")
            }
        }
    )
}
