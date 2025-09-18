package org.michael.kmp.playground.firestore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FlagsViewModel(
    app: Application,
    private val repo: FetchTriggerRepository
) : AndroidViewModel(app) {asdf// 🔑 Usa AndroidViewModel para tener context

    init {
        repo.start()
        viewModelScope.launch {
            repo.triggers.collect { trig ->
                try {
                    performFetch(trig.params)
                    repo.markConsumed(trig.token)
                } catch (_: Throwable) {
                    // no marcamos consumido si falla
                }
            }
        }
    }

    // 👉 aquí haces tu fetch real (por ahora solo un Toast de demo)
    private suspend fun performFetch(params: Map<String, Any?>?) {
        val ctx = getApplication<Application>()
        // Aquí iría tu lógica de fetch (CMS/Backend)
        // ...

        Toast.makeText(ctx, "REALIZAR NUEVA LLAMADA AL CMS!!!!!", Toast.LENGTH_SHORT).show()
    }

    fun manualFetch() {
        viewModelScope.launch {
            performFetch(emptyMap())
        }
    }

    override fun onCleared() {
        repo.stop()
        super.onCleared()
    }
}