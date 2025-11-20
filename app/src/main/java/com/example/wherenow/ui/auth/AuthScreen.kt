package com.example.wherenow.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wherenow.R
import com.example.wherenow.navigation.NavRoutes
import com.example.wherenow.ui.auth.AuthViewModel

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardCapitalization


@Composable
fun AuthScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    val authViewModel: AuthViewModel = viewModel()
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF9C8FFF),
                        Color(0xFFF871FF),
                        Color(0xFFFA8355)
                    )
                )
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = stringResource(R.string.app_name),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = stringResource(R.string.slogan),
                fontSize = 16.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color(0xFFF5F5F5),
                        indicator = {},
                        divider = {}
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text(stringResource(R.string.login_title)) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text(stringResource(R.string.signup_title)) }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ---------------- LOGIN ----------------
                    if (selectedTab == 0) {

                        Text(
                            text = stringResource(R.string.welcome_back),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.signin_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            placeholder = { Text(stringResource(R.string.enter_email)) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                capitalization = KeyboardCapitalization.None,
                                autoCorrect = false
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            placeholder = { Text(stringResource(R.string.enter_password)) },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                capitalization = KeyboardCapitalization.None,
                                autoCorrect = false
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        TextButton(onClick = { /* recuperar contraseña */ }) {
                            Text(stringResource(R.string.forgot_password), color = Color(0xFF6200EE))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                loading = true
                                authViewModel.login(email, password) { ok, msg ->
                                    loading = false
                                    if (ok) {
                                        navController.navigate(NavRoutes.HOME) {
                                            popUpTo(NavRoutes.AUTH) { inclusive = true }
                                        }
                                    } else {
                                        errorMsg = msg ?: "Error al iniciar sesión"
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(stringResource(R.string.login_title), color = Color.White)
                        }
                    }

                    // ---------------- SIGN UP ----------------
                    else {

                        Text(
                            text = stringResource(R.string.join_adventure),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.signup_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            placeholder = { Text(stringResource(R.string.enter_username)) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            placeholder = { Text(stringResource(R.string.enter_fullname)) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            placeholder = { Text(stringResource(R.string.enter_email)) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                capitalization = KeyboardCapitalization.None,
                                autoCorrect = false
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            placeholder = { Text(stringResource(R.string.enter_password)) },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                capitalization = KeyboardCapitalization.None,
                                autoCorrect = false
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            placeholder = { Text(stringResource(R.string.confirm_your_password)) },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (password != confirmPassword) {
                                    errorMsg = "Las contraseñas no coinciden"
                                    return@Button
                                }

                                loading = true
                                authViewModel.signup(email, password, username, fullName) { ok, msg ->
                                    loading = false
                                    if (ok) {
                                        navController.navigate(NavRoutes.LOCATION) {
                                            popUpTo(NavRoutes.AUTH) { inclusive = true }
                                        }
                                    } else {
                                        errorMsg = msg ?: "Error al crear cuenta"
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C8FFF))
                        ) {
                            Text(stringResource(R.string.create_account), color = Color.White)
                        }
                    }

                    // ---- ERROR MESSAGE ----
                    errorMsg?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }
            }
        }

        // ---- LOADING INDICATOR ----
        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}
