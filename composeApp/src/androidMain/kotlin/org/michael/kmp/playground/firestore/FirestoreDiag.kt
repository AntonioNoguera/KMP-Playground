package org.michael.kmp.playground.firestore

import android.content.Context
import android.widget.Toast
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Source

object FirestoreDiag {
    private const val TAG = "FirestoreDiag"

    fun pingServer(context: Context, db: FirebaseFirestore) {

        println("STARTING FIRESTORE PING!!!!!!!!!!!!!!!!!!!!!!!!!!")

        val doc = db.collection("configs").document("appFlags_dev_android")

        // 1) GET directo al servidor (omite cache)
        doc.get(Source.SERVER)
            .addOnSuccessListener { snap ->
                val fromServer = !snap.metadata.isFromCache
                Log.d(TAG, "FIREBALL SERVER GET ok. exists=${snap.exists()} fromServer=$fromServer data=${snap.data}")
                Toast.makeText(
                    context,
                    "Firestore OK (server=${fromServer}, exists=${snap.exists()})",
                    Toast.LENGTH_LONG
                ).show()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "SERVER GET fail: ${e.message}", e)
                Toast.makeText(context, "Firestore FAIL: ${e.message}", Toast.LENGTH_LONG).show()
            }

        // 2) Listener con metadata para ver si llegan updates del servidor
        doc.addSnapshotListener(MetadataChanges.INCLUDE) { snap, err ->
            if (err != null) {
                Log.e(TAG, "Listener error: ${err.message}", err)
                return@addSnapshotListener
            }
            if (snap == null) return@addSnapshotListener
            val fromCache = snap.metadata.isFromCache
            Log.d(TAG, "FIREBALL -> fromCache=$fromCache exists=${snap.exists()} data=${snap.data}")
        }
    }
}