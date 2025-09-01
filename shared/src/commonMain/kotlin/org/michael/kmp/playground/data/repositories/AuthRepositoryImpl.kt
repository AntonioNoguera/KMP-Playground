package org.michael.kmp.playground.data.repositories

import org.michael.kmp.playground.core.network.NetworkClient
import org.michael.kmp.playground.core.network.NetworkResult
import org.michael.kmp.playground.core.network.execute
import org.michael.kmp.playground.data.dto.LoginResponseDto
import org.michael.kmp.playground.data.endpoints.LoginEndpoint
import org.michael.kmp.playground.domain.models.LoginResponse
import org.michael.kmp.playground.domain.repositories.AuthRepository

class AuthRepositoryImpl(
    private val networkClient: NetworkClient
) : AuthRepository {

    override suspend fun login(email: String, password: String): NetworkResult<LoginResponse> {
        // Crear el endpoint específico
        val endpoint = LoginEndpoint(email, password)

        // Ejecutar la petición y mapear al modelo de dominio
        return networkClient.execute<LoginResponseDto>(endpoint)
            .map { it.toDomain() }
    }
}