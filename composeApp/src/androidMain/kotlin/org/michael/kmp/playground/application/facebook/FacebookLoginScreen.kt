package org.michael.kmp.playground.application.facebook

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.michael.kmp.playground.core.network.NetworkResult

@Composable
fun FacebookLoginScreen(
    facebookAuthProvider: FacebookAuthProvider
) {
    var userInfo by remember { mutableStateOf<FacebookUserData?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Botón de Login
        Button(
            onClick = {
                isLoading = true
                errorMessage = null

                // Usar corrutina para el login
                CoroutineScope(Dispatchers.Main).launch {
                    when (val result = facebookAuthProvider.login()) {
                        is NetworkResult.Success -> {
                            userInfo = result.data
                            isLoading = false
                        }

                        //Checkout this handling
                        is NetworkResult.Error -> {
                            errorMessage = "result.message ?: "
                            isLoading = false
                        }
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1877F2) // Facebook Blue
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Puedes agregar un ícono de Facebook aquí
                    Text(
                        text = "Login with Facebook",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de Logout
        if (userInfo != null) {
            Button(
                onClick = {
                    facebookAuthProvider.logout()
                    userInfo = null
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text("Logout", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Mostrar información del usuario
        userInfo?.let { user ->
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Usuario logueado:",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("ID: ${user.id}")
                    Text("Nombre: ${user.name}")
                    Text("Email: ${user.email ?: "No disponible"}")
                    Text("Token: ${user.accessToken.take(20)}...")

                    user.profilePictureUrl?.let { url ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Foto de perfil disponible: ${url.take(30)}...")
                    }
                }
            }
        }

        // Mostrar errores
        errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Error: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

object FacebookUtils {

    fun isValidUserData(userData: FacebookUserData?): Boolean {
        return userData != null &&
                userData.id.isNotEmpty() &&
                userData.name.isNotEmpty() &&
                userData.accessToken.isNotEmpty()
    }

    fun logUserInfo(userData: FacebookUserData) {
        println("=== Facebook User Info ===")
        println("ID: ${userData.id}")
        println("Name: ${userData.name}")
        println("Email: ${userData.email}")
        println("Profile Picture: ${userData.profilePictureUrl}")
        println("Access Token: ${userData.accessToken.take(10)}...")
        println("========================")
    }
}