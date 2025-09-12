package org.michael.kmp.playground.auth.data.endpoints

import org.michael.kmp.playground.core.network.Endpoint
import org.michael.kmp.playground.core.network.EndpointMethod
import org.michael.kmp.playground.auth.domain.usecases.LoginParams

class LoginEndpoint (
    data : LoginParams
): Endpoint {

    override val path: String = "/v1/login"
    override val method: EndpointMethod = EndpointMethod.POST

    override val body: Map<String, Any>? = mapOf(
        "email" to data.email,
        "password" to data.password
    )

    override val headers: Map<String, String> = mapOf(
        "Content-Type" to "application/json"
    )
}