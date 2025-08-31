package org.michael.kmp.playground

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform