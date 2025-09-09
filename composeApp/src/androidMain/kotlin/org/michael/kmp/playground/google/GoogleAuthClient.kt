package org.michael.kmp.playground.google

import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

data class GoogleUserData(
    val userId: String,
    val email: String,
    val displayName: String?,
    val profilePictureUrl: String?,
    val idToken: String
)

class GoogleAuthClientV2(
    private val context: Context,
) {
    private val tag = "GoogleAuthClient: "
    private val credentialManager = CredentialManager.create(context)
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val webClientId = "804204957316-8orqt471rt0qi8vs9b6etg7iukhtdgv9.apps.googleusercontent.com"
    private val webCLient2 = "162144254665-toa3vu2v50m7vs25gc77mjinr55d8fv9.apps.googleusercontent.com"

    suspend fun signInWithFullScreenModal(): GoogleUserData? {
        if (isSignedIn()) {
            println(tag + "already signed in")
            return getCurrentUserData()
        }

        return try {
            println(tag + "starting FULL SCREEN sign in process...")
            val result = buildFullScreenCredentialRequest()
            handleSignIn(result)
        } catch (e: GetCredentialCancellationException) {
            println(tag + "❌ User cancelled full screen modal")
            Toast.makeText(context, "Login cancelado", Toast.LENGTH_SHORT).show()
            null
        } catch (e: NoCredentialException) {
            Toast.makeText(context, "No hay cuentas de Google. Agrega una cuenta en Configuración.", Toast.LENGTH_LONG).show()
            println(tag + "❌ No credentials available")
            null
        } catch (e: GetCredentialException) {
            println(tag + "GetCredentialException: ${e.message}")
            Toast.makeText(context, "Error: ${e.errorMessage}", Toast.LENGTH_LONG).show()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            println(tag + "❌ Unexpected error: ${e.message}")
            Toast.makeText(context, "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
            null
        }
    }

    private suspend fun buildFullScreenCredentialRequest(): GetCredentialResponse {
        println(tag + "building FULL SCREEN credential request...")
        println(tag + "webClientId: $webClientId")

        val googleIdOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
            this.webClientId
        )
            .setNonce(generateNonce()) // Añadir nonce para forzar modal completo
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        println(tag + "requesting credentials from CredentialManager (FULL SCREEN)...")

        return credentialManager.getCredential(
            request = request,
            context = context
        )
    }

    // ==========================================
    // VERSIÓN ALTERNATIVA CON CONFIGURACIÓN ESPECÍFICA
    // ==========================================

    suspend fun signInWithEnhancedModal(): GoogleUserData? {
        return try {
            println(tag + "Enhanced modal sign in...")

            // Configuración más específica para forzar modal completo
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(false)
                .setNonce(generateNonce())
                // Configuraciones adicionales para modal completo
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            // Añadir delay para asegurar que el contexto esté listo
            kotlinx.coroutines.delay(100)

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            handleSignIn(result)

        } catch (e: Exception) {
            println(tag + "Enhanced modal error: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): GoogleUserData? {
        println(tag + "handling sign in result...")
        val credential = result.credential

        return if (
            credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            try {
                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                println(tag + "✅ Token credential created successfully")
                println(tag + "name: ${tokenCredential.displayName}")
                println(tag + "email: ${tokenCredential.id}")

                val userData = GoogleUserData(
                    userId = tokenCredential.id,
                    email = tokenCredential.id,
                    displayName = tokenCredential.displayName,
                    profilePictureUrl = tokenCredential.profilePictureUri?.toString(),
                    idToken = tokenCredential.idToken
                )

                // Firebase auth (opcional)
                //authenticateWithFirebase(tokenCredential.idToken)

                Toast.makeText(context, "¡Bienvenido ${userData.displayName}!", Toast.LENGTH_SHORT).show()
                userData

            } catch (e: GoogleIdTokenParsingException) {
                println(tag + "❌ Token parsing error: ${e.message}")
                null
            }
        } else {
            println(tag + "❌ Invalid credential type: ${credential.type}")
            null
        }
    }

    private suspend fun authenticateWithFirebase(idToken: String) {
        try {
            val authCredential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(authCredential).await()

            if (authResult.user != null) {
                println(tag + "✅ Firebase authentication successful")
            }
        } catch (e: Exception) {
            println(tag + "Firebase auth error: ${e.message}")
            // No fallar si Firebase no funciona
        }
    }

    private fun generateNonce(): String {
        // Generar nonce único para cada request
        return "${System.currentTimeMillis()}_${java.util.UUID.randomUUID()}"
    }

    fun isSignedIn(): Boolean = firebaseAuth.currentUser != null

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    fun getCurrentUserData(): GoogleUserData? {
        val user = firebaseAuth.currentUser ?: return null
        return GoogleUserData(
            userId = user.uid,
            email = user.email ?: "",
            displayName = user.displayName,
            profilePictureUrl = user.photoUrl?.toString(),
            idToken = ""
        )
    }

    suspend fun signOut() {
        firebaseAuth.signOut()
        Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }
}