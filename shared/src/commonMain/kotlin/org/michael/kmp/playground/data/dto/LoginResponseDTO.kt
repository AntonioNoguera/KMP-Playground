package org.michael.kmp.playground.data.dto

import kotlinx.serialization.Serializable
import org.michael.kmp.playground.domain.models.LoginResponse
import org.michael.kmp.playground.domain.models.User

@Serializable
data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
    val role: String
) {
    fun toDomain() = User(
        id = id,
        name = name,
        email = email,
        role = role
    )
}

@Serializable
data class LoginResponseDto(
    val message: String,
    val token: String,
    val user: UserDto
) {
    fun toDomain() = LoginResponse(
        message = message,
        token = token,
        user = user.toDomain()
    )
}