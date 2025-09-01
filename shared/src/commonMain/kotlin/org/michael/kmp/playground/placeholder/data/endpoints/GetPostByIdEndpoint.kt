package org.michael.kmp.playground.placeholder.data.endpoints

import org.michael.kmp.playground.core.network.Endpoint
import org.michael.kmp.playground.core.network.EndpointMethod

class GetPostByIdEndpoint(
    private val postId: Int
) : Endpoint() {

    override val path: String = "posts/$postId"
    override val method: EndpointMethod = EndpointMethod.GET

    override val queryParams: Map<String, String>? = null
    override val body: Map<String, Any>? = null

    override val headers: Map<String, String> = mapOf(
        "Content-Type" to "application/json"
    )
}