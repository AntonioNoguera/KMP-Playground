package org.michael.kmp.playground

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import org.michael.kmp.playground.facebook.FacebookAuthProvider
import org.michael.kmp.playground.google.GoogleFullScreenModalExample
import org.michael.kmp.playground.google.GoogleSignInDebugScreen

class MainActivity : ComponentActivity() {

    private lateinit var facebookAuthProvider: FacebookAuthProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Koin ya está configurado en MainApplication
        println("MainActivity iniciada - Koin disponible")

//        setContent {
//            MaterialTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize().padding(top = 12.dp),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    LoginScreen() // usará koinInject() automáticamente
//                }
//            }
//        }
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(application)

        facebookAuthProvider = FacebookAuthProvider(this)

        setContent {
            MaterialTheme {
                GoogleFullScreenModalExample()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookAuthProvider.handleActivityResult(requestCode, resultCode, data)
    }
}