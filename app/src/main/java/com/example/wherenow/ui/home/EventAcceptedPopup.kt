package com.example.wherenow.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

// Data classes para los datos mock
data class CircleMember(
    val name: String,
    val username: String,
    val description: String,
    val initials: String,
    val circleName: String,
    val mutualFriends: Int,
    val avatarColor: Color
)

data class EventInfo(
    val name: String,
    val description: String,
    val hasPlusOnePrivilege: Boolean
)

@Composable
fun EventAcceptedPopup(
    eventInfo: EventInfo,
    onDismiss: () -> Unit
) {
    var currentView by remember { mutableStateOf(0) } // 0 = confirmation, 1 = select members
    var selectedMembers by remember { mutableStateOf(setOf<String>()) }
    var searchQuery by remember { mutableStateOf("") }
    var personalMessage by remember { mutableStateOf("") }
    var grantPlusOne by remember { mutableStateOf(false) }

    // Mock data para miembros del círculo
    val circleMembersData = remember {
        listOf(
            CircleMember(
                name = "Sarah Chen",
                username = "@sarahc",
                description = "Jazz enthusiast and event organizer",
                initials = "SC",
                circleName = "Music Lovers NYC",
                mutualFriends = 3,
                avatarColor = Color(0xFF9C27B0)
            ),
            CircleMember(
                name = "Mike Rodriguez",
                username = "@mikerod",
                description = "Food blogger and restaurant reviewer",
                initials = "MR",
                circleName = "Foodie Adventures",
                mutualFriends = 7,
                avatarColor = Color(0xFFFF9800)
            ),
            CircleMember(
                name = "Emma Wilson",
                username = "@emmaw",
                description = "Photography enthusiast",
                initials = "EW",
                circleName = "Music Lovers NYC",
                mutualFriends = 5,
                avatarColor = Color(0xFFE91E63)
            ),
            CircleMember(
                name = "David Park",
                username = "@dpark",
                description = "Concert lover and musician",
                initials = "DP",
                circleName = "Music Lovers NYC",
                mutualFriends = 2,
                avatarColor = Color(0xFF2196F3)
            ),
            CircleMember(
                name = "Lisa Martinez",
                username = "@lisamart",
                description = "Culinary explorer",
                initials = "LM",
                circleName = "Foodie Adventures",
                mutualFriends = 4,
                avatarColor = Color(0xFF4CAF50)
            )
        )
    }

    // Filtrar miembros basado en la búsqueda
    val filteredMembers = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            circleMembersData
        } else {
            circleMembersData.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.username.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Botón de cerrar (X)
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF718096)
                        )
                    }

                    // Contenido principal
                    if (currentView == 0) {
                        ConfirmationView(
                            eventInfo = eventInfo,
                            onSendInvitations = { currentView = 1 }
                        )
                    } else {
                        SelectMembersView(
                            members = filteredMembers,
                            selectedMembers = selectedMembers,
                            searchQuery = searchQuery,
                            personalMessage = personalMessage,
                            grantPlusOne = grantPlusOne,
                            onSearchQueryChange = { searchQuery = it },
                            onMemberToggle = { username ->
                                selectedMembers = if (selectedMembers.contains(username)) {
                                    selectedMembers - username
                                } else {
                                    selectedMembers + username
                                }
                            },
                            onPersonalMessageChange = { personalMessage = it },
                            onGrantPlusOneChange = { grantPlusOne = it },
                            onSendInvitations = {
                                // Aquí iría la lógica de envío cuando haya backend
                                onDismiss()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmationView(
    eventInfo: EventInfo,
    onSendInvitations: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mensaje de aceptación
        Text(
            text = "You have been accepted!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9C27B0),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Ícono de check
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFF9C27B0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✓",
                fontSize = 48.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Nombre del evento
        Text(
            text = eventInfo.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9C27B0),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Descripción del evento
        Text(
            text = eventInfo.description,
            fontSize = 14.sp,
            color = Color(0xFF718096),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Plus-One Privilege indicator
        if (eventInfo.hasPlusOnePrivilege) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFF9C27B0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🎁",
                            fontSize = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "You've been granted",
                            fontSize = 12.sp,
                            color = Color(0xFF718096)
                        )
                        Text(
                            text = "Plus-One Privilege",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9C27B0)
                        )
                        Text(
                            text = "Allow invited members to bring a friend",
                            fontSize = 11.sp,
                            color = Color(0xFF718096),
                            lineHeight = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón Send Invitations
        Button(
            onClick = onSendInvitations,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9C27B0)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Send Invitations",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun SelectMembersView(
    members: List<CircleMember>,
    selectedMembers: Set<String>,
    searchQuery: String,
    personalMessage: String,
    grantPlusOne: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onMemberToggle: (String) -> Unit,
    onPersonalMessageChange: (String) -> Unit,
    onGrantPlusOneChange: (Boolean) -> Unit,
    onSendInvitations: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(top = 16.dp)
    ) {
        // Título
        Text(
            text = "Select circle members",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D3748)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search members by name, usernar", fontSize = 14.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF718096)
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF9C27B0),
                unfocusedBorderColor = Color(0xFFE2E8F0)
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Indicador de selección
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Select All (${members.size})",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2D3748)
            )

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFFF9800)
            ) {
                Text(
                    text = "${selectedMembers.size} selected",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de miembros
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(members) { member ->
                MemberCard(
                    member = member,
                    isSelected = selectedMembers.contains(member.username),
                    onToggle = { onMemberToggle(member.username) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Mensaje personal
                Text(
                    text = "Personal Message (Optional)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D3748)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = personalMessage,
                    onValueChange = onPersonalMessageChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Add a personal message to your invitation...", fontSize = 13.sp) },
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF9C27B0),
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Plus-One Privilege toggle
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onGrantPlusOneChange(!grantPlusOne) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (grantPlusOne) Color(0xFFF3E5F5) else Color(0xFFF7FAFC)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    if (grantPlusOne) Color(0xFF9C27B0) else Color(0xFFE2E8F0),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🎁",
                                fontSize = 20.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Include Plus-One Privilege",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3748)
                            )
                            Text(
                                text = "Allow invited members to bring a friend",
                                fontSize = 12.sp,
                                color = Color(0xFF718096)
                            )
                        }

                        Switch(
                            checked = grantPlusOne,
                            onCheckedChange = onGrantPlusOneChange,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF9C27B0)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Send Invitations
                Button(
                    onClick = onSendInvitations,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = selectedMembers.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF9C27B0),
                        disabledContainerColor = Color(0xFFE2E8F0)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Send ${selectedMembers.size} Invitation${if (selectedMembers.size != 1) "s" else ""}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun MemberCard(
    member: CircleMember,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .then(
                if (isSelected) {
                    Modifier.border(2.dp, Color(0xFF9C27B0), RoundedCornerShape(16.dp))
                } else {
                    Modifier
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF3E5F5) else Color(0xFFF7FAFC)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar con iniciales
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(member.avatarColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.initials,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Información del miembro
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = member.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748)
                )

                Text(
                    text = member.username,
                    fontSize = 13.sp,
                    color = Color(0xFF718096)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = member.description,
                    fontSize = 12.sp,
                    color = Color(0xFF718096),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Círculo compartido y amigos mutuos
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(Color(0xFF4CAF50), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "○",
                                fontSize = 10.sp,
                                color = Color.White
                            )
                        }
                        Text(
                            text = member.circleName,
                            fontSize = 11.sp,
                            color = Color(0xFF2D3748)
                        )
                    }

                    Text(
                        text = "${member.mutualFriends} mutual friends",
                        fontSize = 11.sp,
                        color = Color(0xFF718096)
                    )
                }
            }
        }
    }
}