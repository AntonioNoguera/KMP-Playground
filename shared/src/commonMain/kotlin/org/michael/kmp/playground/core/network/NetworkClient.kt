package org.michael.kmp.playground.core.network

interface NetworkClient {
    suspend fun executeRequest(endpoint: Endpoint): NetworkResult<String>
}

// Extension function para deserialización type-safe
suspend inline fun <reified T> NetworkClient.execute(endpoint: Endpoint): NetworkResult<T> {
    return when (val result = executeRequest(endpoint)) {
        is NetworkResult.Success -> {
            try {
                val jsonString = result.data
                println("JSON response: $jsonString")

                val deserializedObject = kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }.decodeFromString<T>(jsonString)

                NetworkResult.Success(deserializedObject)
            } catch (e: Exception) {
                println("Parse error: ${e.message}")
                NetworkResult.Error(NetworkException.ParseException("Parse error: ${e.message}", e))
            }
        }
        is NetworkResult.Error -> result
    }
}
