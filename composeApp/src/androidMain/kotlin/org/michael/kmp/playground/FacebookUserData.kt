package org.michael.kmp.playground

import kotlinx.serialization.Serializable

@Serializable
data class FacebookUserData(
    val id: String,
    val name: String,
    val email: String?,
    val profilePictureUrl: String?,
    val accessToken: String
)