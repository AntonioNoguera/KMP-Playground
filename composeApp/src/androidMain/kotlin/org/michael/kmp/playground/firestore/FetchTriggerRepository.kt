package org.michael.kmp.playground.firestore

import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import androidx.core.content.edit

data class FetchTrigger(val token: Long, val params: Map<String, Any?>)

class FetchTriggerRepository(
    private val db: FirebaseFirestore,
    private val prefs: SharedPreferences,
    private val docPath: String = "configs/appFlags_dev_android"
) {
    companion object {
        private const val TAG = "FIREBALL"
    }

    private var reg: ListenerRegistration? = null
    private val lastSeenKey = "flags.lastSeenToken"

    private val _triggers = MutableSharedFlow<FetchTrigger>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val triggers: SharedFlow<FetchTrigger> = _triggers

    fun start() {
        Log.d(TAG, "start() - Iniciando listener")

        if (reg != null) {
            Log.d(TAG, "start() - Listener ya está activo, retornando")
            return
        }

        val parts = docPath.split("/")
        Log.d(TAG, "start() - Split result: $parts")
        Log.d(TAG, "start() - Parts count: ${parts.size}")

        val (col, doc) = parts.let { it[0] to it[1] }
        Log.d(TAG, "start() - Col extraído: '$col'")
        Log.d(TAG, "start() - Doc extraído: '$doc'")

        // Versión con variables (que no funciona)
        val refWithVars = db.collection(col).document(doc)
        Log.d(TAG, "start() - Ref con variables: ${refWithVars.path}")

        // Versión hardcodeada (que sí funciona)
        val ref = refWithVars

        Log.d(TAG, "start() - Referencia creada: ${ref.path}")

        reg = ref.addSnapshotListener { snap, err ->
            Log.d(TAG, "SnapshotListener - Evento recibido")

            if (err != null) {
                Log.e(TAG, "SnapshotListener - Error: ${err.message}", err)
                return@addSnapshotListener
            }

            if (snap == null) {
                Log.w(TAG, "SnapshotListener - Snapshot es null")
                return@addSnapshotListener
            }

            if (!snap.exists()) {
                Log.w(TAG, "SnapshotListener - Documento no existe")
                return@addSnapshotListener
            }

            Log.d(TAG, "SnapshotListener - Documento válido recibido")
            Log.d(TAG, "SnapshotListener - Datos del documento: ${snap.data}")

            val token = snap.getLong("fetchToken") ?: 0L
            Log.d(TAG, "SnapshotListener - Token actual: $token")

            val lastSeen = prefs.getLong(lastSeenKey, -1L)
            Log.d(TAG, "SnapshotListener - Último token visto: $lastSeen")

            if (token > lastSeen) {
                Log.d(TAG, "SnapshotListener - Token nuevo detectado ($token > $lastSeen)")

                val rawParams = snap.get("params")
                Log.d(TAG, "SnapshotListener - Params raw: $rawParams (tipo: ${rawParams?.javaClass?.simpleName})")

                val params = (rawParams as? Map<*, *>)?.entries
                    ?.mapNotNull { (k, v) ->
                        val key = k as? String
                        if (key != null) {
                            Log.d(TAG, "SnapshotListener - Param procesado: '$key' = $v")
                            key to v
                        } else {
                            Log.w(TAG, "SnapshotListener - Clave no válida ignorada: $k")
                            null
                        }
                    }
                    ?.toMap() ?: emptyMap()

                Log.d(TAG, "SnapshotListener - Params finales: $params")

                val trigger = FetchTrigger(token, params)
                val emitted = _triggers.tryEmit(trigger)

                if (emitted) {
                    Log.d(TAG, "SnapshotListener - Trigger emitido exitosamente: $trigger")
                } else {
                    Log.e(TAG, "SnapshotListener - Error al emitir trigger: $trigger")
                }
            } else {
                Log.d(TAG, "SnapshotListener - Token no es nuevo ($token <= $lastSeen), fetch innecesario")
            }
        }

        Log.d(TAG, "start() - Listener registrado exitosamente")
    }

    fun markConsumed(token: Long) {
        Log.d(TAG, "markConsumed() - Marcando token como consumido: $token")
        val lastSeen = prefs.getLong(lastSeenKey, -1L)
        Log.d(TAG, "markConsumed() - Token anterior: $lastSeen -> Token nuevo: $token")

        prefs.edit { putLong(lastSeenKey, token) }

        Log.d(TAG, "markConsumed() - Token guardado en SharedPreferences")
    }

    fun stop() {
        Log.d(TAG, "stop() - Deteniendo listener")

        if (reg == null) {
            Log.d(TAG, "stop() - No hay listener activo para detener")
        } else {
            reg?.remove()
            reg = null
            Log.d(TAG, "stop() - Listener removido exitosamente")
        }
    }
}