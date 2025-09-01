package org.michael.kmp.playground.core.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

interface NetworkClient {
    // Función principal - recibe un Endpoint y devuelve el resultado
    suspend fun <T : Any> execute(endpoint: Endpoint, responseType: KClass<T>): NetworkResult<T>
}

// Extension function para facilitar el uso (como T::class.java pero para KMP)
suspend inline fun <reified T : Any> NetworkClient.execute(endpoint: Endpoint): NetworkResult<T> {
    return execute(endpoint, T::class)
}


