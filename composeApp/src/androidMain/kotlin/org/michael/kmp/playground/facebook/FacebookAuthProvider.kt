package org.michael.kmp.playground.facebook

import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import org.michael.kmp.playground.core.network.NetworkResult
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FacebookAuthProvider(
    private val activity: Activity
) {
    private val callbackManager = CallbackManager.Factory.create()
    private val loginManager = LoginManager.getInstance()

    suspend fun login(): NetworkResult<FacebookUserData> = suspendCoroutine { continuation ->

        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val accessToken = result.accessToken

                // Hacer petición a Facebook Graph API para obtener información del usuario
                val request = GraphRequest.newMeRequest(accessToken) { jsonObject, response ->
                    try {
                        if (jsonObject != null) {
                            val userData = FacebookUserData(
                                id = jsonObject.getString("id"),
                                name = jsonObject.getString("name"),
                                email = jsonObject.optString("email").takeIf { it.isNotEmpty() },
                                profilePictureUrl = jsonObject.optJSONObject("picture")
                                    ?.optJSONObject("data")
                                    ?.optString("url"),
                                accessToken = accessToken.token
                            )
                            continuation.resume(NetworkResult.Success(userData))
                        } else {
//                            continuation.resume(NetworkResult.Error("No se recibieron datos del usuario"))
                        }
                    } catch (e: Exception) {
//                        continuation.resume(NetworkResult.Error("Error al procesar datos: ${e.message}"))
                    }
                }

                // Especificar los campos que queremos obtener
                val parameters = Bundle()
                parameters.putString("fields", "id,name,email,picture.type(large)")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
                // continuation.resume(NetworkResult.Error("Usuario canceló el login"))
            }

            override fun onError(error: FacebookException) {
                // continuation.resume(NetworkResult.Error("Error de Facebook: ${error.message}"))
            }
        })

        // Iniciar el proceso de login
        loginManager.logInWithReadPermissions(activity, listOf("email", "public_profile"))
    }

    fun logout() {
        loginManager.logOut()
    }

    fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    fun getCurrentAccessToken(): String? {
        return AccessToken.getCurrentAccessToken()?.token
    }

    fun getCurrentUserInfo(): FacebookUserData? {
        val accessToken = AccessToken.getCurrentAccessToken()
        return if (accessToken != null && !accessToken.isExpired) {
            // Aquí podrías hacer otra llamada a Graph API si necesitas datos frescos
            // Por ahora retornamos null, ya que necesitarías hacer una llamada async
            null
        } else {
            null
        }
    }

    // IMPORTANTE: Este método debe ser llamado desde onActivityResult de tu Activity
    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}