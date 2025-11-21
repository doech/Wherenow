package com.example.wherenow.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wherenow.ui.categories.CategoryViewModel
import com.example.wherenow.data.model.CategoryRow
import com.example.wherenow.ui.auth.AuthViewModel
import com.example.wherenow.ui.circles.CircleViewModel
import kotlinx.coroutines.delay

@Composable
fun CreateCircle(
    onDismiss: () -> Unit
) {
    val categoryVM: CategoryViewModel = viewModel()
    val authVM: AuthViewModel = viewModel()
    val circleVM: CircleViewModel = viewModel()

    val categories by categoryVM.categories.collectAsState()
    val user by authVM.user.collectAsState()

    LaunchedEffect(Unit) {
        categoryVM.loadCategories()
    }

    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var successMessage by remember { mutableStateOf<String?>(null) }

    // ⭐ bandera para cerrar después de éxito
    var closeAfterSuccess by remember { mutableStateOf(false) }

    // ⭐ ESTE LaunchedEffect sí es válido
    LaunchedEffect(closeAfterSuccess) {
        if (closeAfterSuccess) {
            delay(1200)
            onDismiss()
        }
    }

    fun CategoryRow.displayName(): String {
        return this.name["en"] ?: this.name["es"] ?: "Unnamed"
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = Color(0xFFF3E5F5),
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Create new circle", style = MaterialTheme.typography.titleLarge)

                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Circle Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(Modifier.height(16.dp))

                Text("Category", style = MaterialTheme.typography.titleSmall)

                Spacer(Modifier.height(6.dp))

                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val displayName = selectedCategory?.let { id ->
                            categories.find { it.id == id }?.displayName()
                        } ?: "Choose a category"

                        Text(displayName)
                        Icon(Icons.Default.ArrowDropDown, null)
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.displayName()) },
                            onClick = {
                                selectedCategory = cat.id
                                expanded = false
                            }
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                if (loading) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(16.dp))
                }

                errorMessage?.let {
                    Text(it, color = Color.Red)
                    Spacer(Modifier.height(8.dp))
                }

                successMessage?.let {
                    Text(
                        text = it,
                        color = Color(0xFF2E7D32),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Button(
                    enabled = name.isNotBlank() && selectedCategory != null && user != null && !loading,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        loading = true
                        errorMessage = null

                        val creatorId = user?.id ?: return@Button

                        circleVM.createCircle(
                            name = name.trim(),
                            desc = desc.trim(),
                            categoryId = selectedCategory!!,
                            creatorId = creatorId
                        ) { ok, msg ->
                            loading = false
                            if (ok) {
                                successMessage = "Circle created successfully!"
                                closeAfterSuccess = true  // ⭐ solo activamos la bandera
                            } else {
                                errorMessage = msg ?: "Error creating circle"
                            }
                        }
                    }
                ) {
                    Text("Create Circle")
                }

                Spacer(Modifier.height(8.dp))

                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    }
}
