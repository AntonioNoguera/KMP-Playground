package org.michael.kmp.playground.placeholder.domain.usecases

import org.michael.kmp.playground.core.network.NetworkException
import org.michael.kmp.playground.core.network.NetworkResult
import org.michael.kmp.playground.placeholder.domain.models.PostModel
import org.michael.kmp.playground.placeholder.domain.repositories.PostRepository

class GetPostByIdUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: Int): NetworkResult<PostModel> {
        // Bussiness Rules
        if (postId <= 0) {
            return NetworkResult.Error(
                NetworkException.UnknownException("El ID del post debe ser mayor a 0")
            )
        }

        return postRepository.getPostById(postId)
    }
}