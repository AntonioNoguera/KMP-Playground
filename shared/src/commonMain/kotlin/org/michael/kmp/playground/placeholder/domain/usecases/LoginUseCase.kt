package org.michael.kmp.playground.placeholder.domain.usecases

import org.michael.kmp.playground.core.network.NetworkResult
import org.michael.kmp.playground.core.network.UseCase
import org.michael.kmp.playground.placeholder.domain.models.AuthModel
import org.michael.kmp.playground.placeholder.domain.repositories.AuthRepository

data class LoginParams(
    val email: String,
    val password: String
)

class LoginUseCase(
    private val authRepository: AuthRepository
): UseCase<LoginParams, AuthModel> {
    override suspend operator fun invoke(params: LoginParams) : NetworkResult<AuthModel> {
        return authRepository.login(params.email, params.password)
    }
}