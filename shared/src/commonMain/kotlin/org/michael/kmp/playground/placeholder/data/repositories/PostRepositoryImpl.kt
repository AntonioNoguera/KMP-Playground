package org.michael.kmp.playground.placeholder.data.repositories

import org.michael.kmp.playground.core.network.NetworkClient
import org.michael.kmp.playground.core.network.NetworkResult
import org.michael.kmp.playground.core.network.execute
import org.michael.kmp.playground.placeholder.data.dto.PostDTO
import org.michael.kmp.playground.placeholder.data.endpoints.GetPostByIdEndpoint
import org.michael.kmp.playground.placeholder.domain.models.PostModel
import org.michael.kmp.playground.placeholder.domain.repositories.PostRepository

class PostRepositoryImpl(
    private val networkClient: NetworkClient
) : PostRepository {

    override suspend fun getPostById(id: Int): NetworkResult<PostModel> {

        val endpoint = GetPostByIdEndpoint(id)

        return networkClient.execute<PostDTO>(endpoint)
            .map { it.toDomain() }
    }
}