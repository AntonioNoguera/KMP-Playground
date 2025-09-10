package org.michael.kmp.playground.firestore

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun FlagsScreen(
    vm: FlagsViewModel = koinViewModel()
) {
    var status by remember { mutableStateOf("Esperando trigger...") }
    var isLoading by remember { mutableStateOf(false) }


    // Escucha los logs del fetch en el VM (si agregas callbacks)
    // Aquí lo hacemos simple: cuando arranca un fetch, cambia el estado.
    LaunchedEffect(Unit) {
        // podrías exponer un Flow en el VM y recolectarlo aquí
    }

    Scaffold(
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = status,
                style = MaterialTheme.typography.bodyLarge
            )

            if (isLoading) {
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            Spacer(Modifier.height(24.dp))

            Button(onClick = {
                isLoading = true
                status = "Ejecutando fetch manual..."
                // Simula un fetch manual, en producción llamarías al VM
                vm.manualFetch()
                // Demo: cambia el estado de nuevo tras 1s

            }) {
                Text("Probar fetch manual")
            }
        }
    }
}