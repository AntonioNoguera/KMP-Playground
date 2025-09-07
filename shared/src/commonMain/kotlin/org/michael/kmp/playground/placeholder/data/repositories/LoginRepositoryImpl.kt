package org.michael.kmp.playground.placeholder.data.repositories

import org.michael.kmp.playground.core.network.NetworkClient
import org.michael.kmp.playground.core.network.NetworkResult
import org.michael.kmp.playground.core.network.execute
import org.michael.kmp.playground.placeholder.data.dto.AuthDTO
import org.michael.kmp.playground.placeholder.data.endpoints.LoginEndpoint
import org.michael.kmp.playground.placeholder.domain.models.AuthModel
import org.michael.kmp.playground.placeholder.domain.repositories.AuthRepository
import org.michael.kmp.playground.placeholder.domain.usecases.LoginParams

class LoginRepositoryImpl (
    private val networkClient: NetworkClient
    ): AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): NetworkResult<AuthModel> {

        val endpoint = LoginEndpoint(LoginParams(email, password))

        return networkClient.execute<AuthDTO>(endpoint)
            .map { it.toDomain() }
    }
}