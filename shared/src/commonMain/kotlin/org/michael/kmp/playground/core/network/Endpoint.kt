package org.michael.kmp.playground.core.network

abstract class Endpoint {
    // Propiedades obligatorias que cada endpoint debe definir
    abstract val path: String
    abstract val method: EndpointMethod

    // Propiedades opcionales con valores por defecto (como en tu Swift)
    open val queryParams: Map<String, String>? = null
    open val body: Map<String, Any>? = null
    open val headers: Map<String, String>? = null
}