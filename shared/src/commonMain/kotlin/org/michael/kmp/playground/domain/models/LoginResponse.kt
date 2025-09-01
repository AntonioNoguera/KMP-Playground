package org.michael.kmp.playground.domain.models
 

data class LoginResponse(
    val message: String,
    val token: String,
    val user: User
)