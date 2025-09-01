package org.michael.kmp.playground.data.endpoints

import org.michael.kmp.playground.core.network.Endpoint
import org.michael.kmp.playground.core.network.EndpointMethod

class LoginEndpoint(
    private val email: String,
    private val password: String
) : Endpoint() {

    override val path: String = "v1/login"
    override val method: EndpointMethod = EndpointMethod.POST

    override val body: Map<String, Any> = mapOf(
        "email" to email,
        "password" to password
    )

    override val headers: Map<String, String> = mapOf(
        "Content-Type" to "application/json"
    )
}