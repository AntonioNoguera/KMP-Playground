package org.michael.kmp.playground.core.network

sealed class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    // Error HTTP (400, 404, 500, etc.)
    class HttpException(val code: Int, message: String) : NetworkException("HTTP $code: $message")

    // Error de conexión general
    class NetworkError(message: String, cause: Throwable? = null) : NetworkException(message, cause)

    // Error al parsear la respuesta JSON
    class ParseException(message: String, cause: Throwable? = null) : NetworkException(message, cause)

    // Timeout de la petición
    class TimeoutException : NetworkException("Request timeout")

    // Sin conexión a internet
    class NoInternetException : NetworkException("No internet connection")

    // Error desconocido
    class UnknownException(message: String) : NetworkException(message)
}