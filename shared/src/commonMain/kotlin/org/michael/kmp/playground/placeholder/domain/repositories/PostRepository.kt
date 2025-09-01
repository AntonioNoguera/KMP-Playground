package org.michael.kmp.playground.placeholder.domain.repositories

import org.michael.kmp.playground.core.network.NetworkResult
import org.michael.kmp.playground.placeholder.domain.models.PostModel

interface PostRepository {
    suspend fun getPostById(id: Int): NetworkResult<PostModel>
}