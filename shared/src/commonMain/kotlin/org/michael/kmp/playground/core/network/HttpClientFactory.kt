package org.michael.kmp.playground.core.network

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


// Función expect - cada plataforma implementará su versión
expect fun createHttpClient(): HttpClient

// Configuración común que aplica a todas las plataformas
fun HttpClient.configureClient() = config {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
        })
    }

    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.INFO
        filter { request ->
            request.url.host.contains("jsonplaceholder") ||
                    request.url.host.contains("yourapi")
        }
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30000
        connectTimeoutMillis = 30000
        socketTimeoutMillis = 30000
    }
}