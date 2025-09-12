package org.michael.kmp.playground.auth.domain.usecases

import org.michael.kmp.playground.core.network.NetworkResult
import org.michael.kmp.playground.core.network.UseCase
import org.michael.kmp.playground.auth.domain.models.AuthModel
import org.michael.kmp.playground.auth.domain.repositories.AuthRepository

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