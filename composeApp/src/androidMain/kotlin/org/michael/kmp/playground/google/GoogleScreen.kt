package org.michael.kmp.playground.google

import android.R.attr.bottom
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GoogleSignInDebugScreen() {
    val context = LocalContext.current
    val googleAuthClient = remember { GoogleAuthClient(context) }

    var isSignedIn by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var debugInfo by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        googleAuthClient.debugConfiguration()
        isSignedIn = googleAuthClient.isSignedIn()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Google Sign In Debug",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Información de debug
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Estado actual:",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text("Signed in: $isSignedIn")

                if (isSignedIn) {
                    val currentUser = googleAuthClient.getCurrentUser()
                    Text("User: ${currentUser?.email}")
                    Text("Name: ${currentUser?.displayName}")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de acción
        if (!isSignedIn) {
            Button(
                onClick = {
                    isLoading = true
                    errorMessage = null

                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            val success = googleAuthClient.signIn()
                            isSignedIn = success

                            if (!success) {
                                errorMessage = "Sign in failed - check logs for details"
                            }
                        } catch (e: Exception) {
                            errorMessage = "Exception: ${e.message}"
                            e.printStackTrace()
                        }
                        isLoading = false
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text("Sign In with Google")
                }
            }
        } else {
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        googleAuthClient.signOut()
                        isSignedIn = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Out")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de debug
        Button(
            onClick = {
                googleAuthClient.debugConfiguration()
                val playServicesOk = googleAuthClient.checkGooglePlayServices()
                debugInfo = "Google Play Services: $playServicesOk\nCheck logs for more details"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Debug Configuration")
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

        // Mostrar info de debug
        if (debugInfo.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = debugInfo,
                    color = Color.Blue,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}