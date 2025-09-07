package org.michael.kmp.playground

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.koinInject

@Composable
fun LoginScreen(
    viewModel: PostsViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Login Multiplatform",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Clean Architecture + KMP + Koin DI + MVVM",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                uiState.data == null && !uiState.isLoading -> {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TextField(
                            value = username,
                            onValueChange = { newValue -> username = newValue },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Usuario") },
                            placeholder = { Text("ej: mike") }
                        )

                        TextField(
                            value = password,
                            onValueChange = { newValue -> password = newValue },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Contraseña") },
                            placeholder = { Text("***") }
                        )

                        Button(
                            onClick = { viewModel.login(username, password) },
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .align(Alignment.End)
                        ) {
                            Text("Iniciar Sesión")
                        }
                    }
                }

                uiState.isLoading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Leyendo petición...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                uiState.errorMessage != null -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFCDD2)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp) // padding interno
                        ) {
                            Text(
                                text = "Problemas al cargar el login!!",
                                color = Color(0xFFFFFFFF)
                            )
                        }
                    }
                }

                uiState.data != null -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFC8E6C9)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text("Login exitoso! Token: ${uiState.data?.token}")
                        }
                    }
                }
            }
        }
    }
}