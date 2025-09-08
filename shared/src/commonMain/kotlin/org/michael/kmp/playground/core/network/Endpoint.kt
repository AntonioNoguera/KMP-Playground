package org.michael.kmp.playground.core.network

interface Endpoint {
    val path: String
    val method: EndpointMethod
    val queryParams: Map<String, String>? get() = null
    val body: Map<String, Any>? get() = null
    val headers: Map<String, String>? get() = null
}