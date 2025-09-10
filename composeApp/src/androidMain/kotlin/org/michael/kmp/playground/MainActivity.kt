package org.michael.kmp.playground

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.android.inject
import org.michael.kmp.playground.application.facebook.FacebookAuthProvider
import org.michael.kmp.playground.application.google.GoogleFullScreenModalExample
import org.michael.kmp.playground.firestore.FlagsScreen
import org.michael.kmp.playground.firestore.FirestoreDiag

class MainActivity : ComponentActivity() {

    private lateinit var facebookAuthProvider: FacebookAuthProvider


    private val firestore: FirebaseFirestore by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirestoreDiag.pingServer(this, firestore)


        // Koin ya está configurado en MainApplication
        println("MainActivity iniciada - Koin disponible")


        //LoginScreen() // inyecta automáticamente
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(application)

        facebookAuthProvider = FacebookAuthProvider(this)

        setContent {
            MaterialTheme {
                FlagsScreen()
            }
        }

    }

    //For facebook handling?
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookAuthProvider.handleActivityResult(requestCode, resultCode, data)
    }
}