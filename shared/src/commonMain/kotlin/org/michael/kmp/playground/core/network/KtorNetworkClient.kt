package org.michael.kmp.playground.core.network

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
class KtorNetworkClient(
    private val baseUrl: String,
    private val httpClient: HttpClient
) : NetworkClient {

    override suspend fun executeRequest(endpoint: Endpoint): NetworkResult<String> {
        return withContext(Dispatchers.Default) {
            try {
                val fullUrl = if (endpoint.path.startsWith("http")) {
                    endpoint.path
                } else {
                    "$baseUrl/${endpoint.path.trimStart('/')}"
                }

                println("Making request to: $fullUrl")

                val response = httpClient.request(fullUrl) {
                    method = when (endpoint.method) {
                        EndpointMethod.GET -> HttpMethod.Get
                        EndpointMethod.POST -> HttpMethod.Post
                        EndpointMethod.PUT -> HttpMethod.Put
                        EndpointMethod.DELETE -> HttpMethod.Delete
                    }

                    endpoint.headers?.forEach { (key, value) ->
                        header(key, value)
                    }

                    endpoint.queryParams?.forEach { (key, value) ->
                        parameter(key, value)
                    }

                    if (endpoint.body != null &&
                        (endpoint.method == EndpointMethod.POST || endpoint.method == EndpointMethod.PUT)) {
                        contentType(ContentType.Application.Json)
                        setBody(endpoint.body)
                    }
                }

                val responseText = response.bodyAsText()
                println("Response received successfully")

                NetworkResult.Success(responseText)

            } catch (e: Exception) {
                println("Network error: ${e.message}")
                NetworkResult.Error(mapException(e))
            }
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
            //TODO: Hace falta el método de verificación de "no internet"
            else -> NetworkException.NetworkError("Network error: ${exception.message}", exception)
        }
    }
}