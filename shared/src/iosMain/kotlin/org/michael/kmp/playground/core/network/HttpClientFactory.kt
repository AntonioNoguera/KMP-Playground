package org.michael.kmp.playground.core.network


import io.ktor.client.*
import io.ktor.client.engine.darwin.*

actual fun createHttpClient(): HttpClient {
    return HttpClient(Darwin) {
        engine {
            configureRequest {
                setAllowsCellularAccess(true)
                setAllowsConstrainedNetworkAccess(true)
            }
        }
    }.configureClient()
}