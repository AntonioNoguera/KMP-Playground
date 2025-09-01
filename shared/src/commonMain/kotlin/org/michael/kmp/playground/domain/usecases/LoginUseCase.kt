package org.michael.kmp.playground.domain.usecases

import org.michael.kmp.playground.core.network.NetworkResult
import org.michael.kmp.playground.domain.models.LoginResponse
import org.michael.kmp.playground.domain.repositories.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): NetworkResult<LoginResponse> {
        // Aquí puedes agregar validaciones de negocio si es necesario
        if (email.isBlank() || password.isBlank()) {
            return NetworkResult.Error(
                org.michael.kmp.playground.core.network.NetworkException.UnknownException(
                    "Email y password son requeridos"
                )
            )
        }

        return authRepository.login(email, password)
    }
}