package org.michael.kmp.playground.placeholder.domain.usecases

import org.michael.kmp.playground.core.network.NetworkResult
import org.michael.kmp.playground.placeholder.domain.models.AuthModel
import org.michael.kmp.playground.placeholder.domain.repositories.AuthRepository

data class LoginParams(
    val email: String,
    val password: String
)

//TODO: Hacer que esta clase extienda de una clase principal use case, donde los params y el response sean propiedades del use case
class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(params: LoginParams) : NetworkResult<AuthModel> {
        return authRepository.login(params.email, params.password)
    }
}