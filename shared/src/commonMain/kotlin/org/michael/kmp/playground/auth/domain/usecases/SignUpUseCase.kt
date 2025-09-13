package org.michael.kmp.playground.auth.domain.usecases

import org.michael.kmp.playground.auth.domain.models.AuthModel
import org.michael.kmp.playground.auth.domain.repositories.AuthRepository
import org.michael.kmp.playground.core.network.NetworkResult
import org.michael.kmp.playground.core.network.UseCase

data class SignUpCase(
    val email: String,
    val password: String
)

class SignUpUseCase(
    private val authRepository: AuthRepository
): UseCase<LoginParams, AuthModel> {
    override suspend operator fun invoke(params: LoginParams) : NetworkResult<AuthModel> {
        return authRepository.signUp(params.email, params.password)
    }
}