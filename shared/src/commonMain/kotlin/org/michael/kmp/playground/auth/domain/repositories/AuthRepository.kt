package org.michael.kmp.playground.auth.domain.repositories

import org.michael.kmp.playground.core.network.NetworkResult
import org.michael.kmp.playground.auth.domain.models.AuthModel

interface AuthRepository {
    suspend fun login(email: String, password: String) : NetworkResult<AuthModel>
}