package com.mike_dev.kotlinplayground

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform