package org.michael.kmp.playground.core.network

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass
import kotlin.reflect.KType

class KtorNetworkClient(
    private val baseUrl: String,
    private val httpClient: HttpClient
) : NetworkClient {

    override suspend fun <T : Any> execute(endpoint: Endpoint, responseType: KClass<T>): NetworkResult<T> {
        return withContext(Dispatchers.Default) {
            try {
                // Construir URL completa
                val fullUrl = if (endpoint.path.startsWith("http")) {
                    endpoint.path
                } else {
                    "$baseUrl/${endpoint.path.trimStart('/')}"
                }

                // Hacer la petición HTTP
                val response = httpClient.request(fullUrl) {
                    // Configurar método HTTP
                    method = when (endpoint.method) {
                        EndpointMethod.GET -> HttpMethod.Get
                        EndpointMethod.POST -> HttpMethod.Post
                        EndpointMethod.PUT -> HttpMethod.Put
                        EndpointMethod.DELETE -> HttpMethod.Delete
                    }

                    // Agregar headers
                    endpoint.headers?.forEach { (key, value) ->
                        header(key, value)
                    }

                    // Agregar query parameters
                    endpoint.queryParams?.forEach { (key, value) ->
                        parameter(key, value)
                    }

                    // Agregar body para POST/PUT
                    if (endpoint.body != null &&
                        (endpoint.method == EndpointMethod.POST || endpoint.method == EndpointMethod.PUT)) {
                        contentType(ContentType.Application.Json)
                        setBody(endpoint.body)
                    }
                }

                // Parsear respuesta
                parseResponse(response, responseType)

            } catch (e: Exception) {
                NetworkResult.Error(mapException(e))
            }
        }
    }

    private suspend fun <T : Any> parseResponse(response: HttpResponse, responseType: KClass<T>): NetworkResult<T> {
        return try {
            val result = when (responseType) {
                String::class -> {
                    @Suppress("UNCHECKED_CAST")
                    response.bodyAsText() as T
                }
                else -> {
                    val jsonString = response.bodyAsText()
                    kotlinx.serialization.json.Json.decodeFromString(
                        kotlinx.serialization.serializer(responseType as KType),
                        jsonString
                    ) as T
                }
            }
            NetworkResult.Success(result)
        } catch (e: Exception) {
            NetworkResult.Error(NetworkException.ParseException("Failed to parse response: ${e.message}", e))
        }
    }

    private fun mapException(exception: Exception): NetworkException {
        return when (exception) {
            is ClientRequestException -> NetworkException.HttpException(
                code = exception.response.status.value,
                message = exception.message
            )
            is ServerResponseException -> NetworkException.HttpException(
                code = exception.response.status.value,
                message = exception.message
            )
            is RedirectResponseException -> NetworkException.HttpException(
                code = exception.response.status.value,
                message = exception.message
            )
            is HttpRequestTimeoutException -> NetworkException.TimeoutException()
            else -> NetworkException.NetworkError("Network error: ${exception.message}", exception)
        }
    }
}