package org.michael.kmp.playground.placeholder.di

import org.koin.dsl.module
import org.michael.kmp.playground.core.network.KtorNetworkClient
import org.michael.kmp.playground.core.network.NetworkClient
import org.michael.kmp.playground.core.network.createHttpClient
import org.michael.kmp.playground.placeholder.data.repositories.PostRepositoryImpl
import org.michael.kmp.playground.placeholder.domain.repositories.PostRepository
import org.michael.kmp.playground.placeholder.domain.usecases.GetPostByIdUseCase

val networkModule = module {
    single { createHttpClient() }

    single<NetworkClient> {
        KtorNetworkClient(
            baseUrl = "https://jsonplaceholder.typicode.com",
            httpClient = get()
        )
    }
}

// Módulo de repositorios
val repositoryModule = module {
    single<PostRepository> { PostRepositoryImpl(get()) }
}

// Módulo de casos de uso
val useCaseModule = module {
    single { GetPostByIdUseCase(get()) }
}

// Lista de todos los módulos
val allModules = listOf(
    networkModule,
    repositoryModule,
    useCaseModule
)