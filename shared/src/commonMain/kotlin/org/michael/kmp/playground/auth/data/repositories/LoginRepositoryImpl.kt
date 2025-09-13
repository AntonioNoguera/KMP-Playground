package org.michael.kmp.playground.auth.data.repositories

import org.michael.kmp.playground.core.network.NetworkClient
import org.michael.kmp.playground.core.network.NetworkResult
import org.michael.kmp.playground.core.network.execute
import org.michael.kmp.playground.auth.data.dto.AuthDTO
import org.michael.kmp.playground.auth.data.endpoints.LoginEndpoint
import org.michael.kmp.playground.auth.data.endpoints.SignUpEndpoint
import org.michael.kmp.playground.auth.domain.models.AuthModel
import org.michael.kmp.playground.auth.domain.repositories.AuthRepository
import org.michael.kmp.playground.auth.domain.usecases.LoginParams

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

    override suspend fun signUp(email: String, password: String): NetworkResult<AuthModel> {
        val endpoint = SignUpEndpoint(data = LoginParams(email, password))
        return networkClient.execute<AuthDTO>(endpoint)
            .map { it.toDomain() }
    }


}