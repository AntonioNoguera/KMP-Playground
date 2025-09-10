package org.michael.kmp.playground.application.google

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun GoogleFullScreenModalExample() {
    val context = LocalContext.current
    val googleAuthClient = remember { GoogleAuthClientV2(context) }

    var currentUser by remember { mutableStateOf<GoogleUserData?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (googleAuthClient.isSignedIn()) {
            currentUser = googleAuthClient.getCurrentUserData()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        if (currentUser == null) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Inicia sesión para continuar",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 48.dp)
                )

                // Botón que abre MODAL COMPLETO
                Button(
                    onClick = {
                        isLoading = true
                        errorMessage = null

                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                // 🔥 Método específico para modal completo
                                val userData = googleAuthClient.signInWithFullScreenModal()

                                if (userData != null) {
                                    currentUser = userData
                                } else {
                                    errorMessage = "No se pudo completar el inicio de sesión"
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error: ${e.message}"
                                e.printStackTrace()
                            }
                            isLoading = false
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Google Icon
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "G",
                                    color = Color(0xFF4285F4),
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "Continuar con Google",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón alternativo con configuración enhanced
                OutlinedButton(
                    onClick = {
                        isLoading = true
                        CoroutineScope(Dispatchers.Main).launch {
                            val userData = googleAuthClient.signInWithEnhancedModal()
                            currentUser = userData
                            isLoading = false
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Probar Modal Enhanced")
                }

                errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = error,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            }

        } else {
            // Mostrar usuario logueado
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "✅ ¡Logueado con éxito!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF4285F4)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Nombre: ${currentUser!!.displayName}")
                    Text("Email: ${currentUser!!.email}")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                googleAuthClient.signOut()
                                currentUser = null
                            }
                        }
                    ) {
                        Text("Cerrar Sesión")
                    }
                }
            }
        }
    }
}