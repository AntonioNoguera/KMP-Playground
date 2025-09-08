package org.michael.kmp.playground

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

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

        // Crear el provider
        facebookAuthProvider = FacebookAuthProvider(this)

        setContent {
            MaterialTheme {
                FacebookLoginScreen(facebookAuthProvider)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookAuthProvider.handleActivityResult(requestCode, resultCode, data)
    }
}