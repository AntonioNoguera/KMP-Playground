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
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import org.michael.kmp.playground.R

class GoogleAuthClient(
    private val context: Context,
) {
    private val tag = "GoogleAuthClient: "
    private val credentialManager = CredentialManager.create(context)
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val webClientId = "804204957316-8orqt471rt0qi8vs9b6etg7iukhtdgv9.apps.googleusercontent.com"

    fun isSignedIn(): Boolean {
        val isSignedIn = firebaseAuth.currentUser != null
        println(tag + "isSignedIn: $isSignedIn")
        return isSignedIn
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    suspend fun signIn(): Boolean {
        if (isSignedIn()) {
            println(tag + "already signed in")
            return true
        }

        return try {
            println(tag + "starting sign in process...")
            val result = buildCredentialRequest()
            handleSignIn(result)
        } catch (e: NoCredentialException) {
            Toast.makeText(context, "No hay cuentas de google en el dipo", Toast.LENGTH_SHORT).show()
            println(tag + "NoCredentialException: ${e.message}")
            println(tag + "❌ No hay cuentas de Google en el dispositivo")
            false
        } catch (e: GetCredentialException) {
            println(tag + "GetCredentialException: ${e.message}")
            println(tag + "❌ Error obteniendo credenciales: ${e.errorMessage}")
            false
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            println(tag + "❌ signIn error: ${e.message}")
            false
        }
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): Boolean {
        println(tag + "handling sign in result...")
        val credential = result.credential

        println(tag + "credential type: ${credential.type}")

        return if (
            credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            try {
                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                println(tag + "✅ Token credential created successfully")
                println(tag + "name: ${tokenCredential.displayName}")
                println(tag + "email: ${tokenCredential.id}")
                println(tag + "image: ${tokenCredential.profilePictureUri}")

                // Autenticar con Firebase
                val authCredential = GoogleAuthProvider.getCredential(
                    tokenCredential.idToken, null
                )

                println(tag + "authenticating with Firebase...")
                val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                val success = authResult.user != null

                if (success) {
                    println(tag + "✅ Firebase authentication successful")
                    println(tag + "Firebase user: ${authResult.user?.email}")
                } else {
                    println(tag + "❌ Firebase authentication failed")
                }

                success

            } catch (e: GoogleIdTokenParsingException) {
                println(tag + "❌ GoogleIdTokenParsingException: ${e.message}")
                false
            } catch (e: Exception) {
                println(tag + "❌ Error in handleSignIn: ${e.message}")
                e.printStackTrace()
                false
            }
        } else {
            println(tag + "❌ credential is not GoogleIdTokenCredential")
            println(tag + "credential type: ${credential.type}")
            false
        }
    }

    private suspend fun buildCredentialRequest(): GetCredentialResponse {
        println(tag + "building credential request...")
        println(tag + "webClientId: $webClientId")

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(this.context.getString(R.string.webClientId))
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        println(tag + "requesting credentials from CredentialManager...")

        return credentialManager.getCredential(
            request = request,
            context = context
        )
    }

    suspend fun signOut() {
        try {
            firebaseAuth.signOut()
            println(tag + "✅ signed out successfully")
        } catch (e: Exception) {
            println(tag + "❌ error signing out: ${e.message}")
        }
    }


    fun debugConfiguration() {
        println(tag + "=== GOOGLE AUTH DEBUG ===")
        println(tag + "Context: ${context::class.simpleName}")
        println(tag + "Web Client ID: $webClientId")
        println(tag + "Firebase Auth initialized: ${FirebaseAuth.getInstance() != null}")
        println(tag + "Current user: ${firebaseAuth.currentUser?.email ?: "null"}")
        println(tag + "Package name: ${context.packageName}")
        println(tag + "=========================")
    }

    // Para verificar si Google Play Services está disponible
    fun checkGooglePlayServices(): Boolean {
        return try {
            val googleApiAvailability = GoogleApiAvailability.getInstance()
            val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
            val isAvailable = resultCode == ConnectionResult.SUCCESS

            println(tag + "Google Play Services available: $isAvailable")
            if (!isAvailable) {
                println(tag + "Google Play Services error code: $resultCode")
            }

            isAvailable
        } catch (e: Exception) {
            println(tag + "Error checking Google Play Services: ${e.message}")
            false
        }
    }
}