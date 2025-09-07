package org.michael.kmp.playground.placeholder.data.dto

import kotlinx.serialization.Serializable
import org.michael.kmp.playground.placeholder.domain.models.AuthModel

@Serializable
data class AuthDTO (
    val token: String
) {
    fun toDomain() = AuthModel(
        token = token
    )
}