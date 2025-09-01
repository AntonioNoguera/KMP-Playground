package org.michael.kmp.playground.domain.repositories

import org.michael.kmp.playground.core.network.NetworkResult
import org.michael.kmp.playground.domain.models.LoginResponse

interface AuthRepository {
    suspend fun login(email: String, password: String): NetworkResult<LoginResponse>
}