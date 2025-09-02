package org.michael.kmp.playground.placeholder.data.dto

import kotlinx.serialization.Serializable
import org.michael.kmp.playground.placeholder.domain.models.PostModel

@Serializable
data class PostDTO(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
) {
    fun toDomain() = PostModel(
        userId = userId,
        id = id,
        title = title,
        body = body
    )
}