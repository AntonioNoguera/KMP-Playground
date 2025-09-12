package org.michael.kmp.playground.auth.data.dto

import kotlinx.serialization.Serializable
import org.michael.kmp.playground.auth.domain.models.AuthModel

@Serializable
data class AuthDTO (
    val token: String
) {
    fun toDomain() = AuthModel(
        token = token
    )
}