package org.michael.kmp.playground.placeholder.domain.repositories

import org.michael.kmp.playground.core.network.NetworkResult
import org.michael.kmp.playground.placeholder.domain.models.AuthModel

interface AuthRepository {
    suspend fun login(email: String, password: String) : NetworkResult<AuthModel>
}