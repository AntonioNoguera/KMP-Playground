package org.michael.kmp.playground.di

import org.koin.dsl.module
import org.michael.kmp.playground.core.network.KtorNetworkClient
import org.michael.kmp.playground.core.network.NetworkClient
import org.michael.kmp.playground.core.network.createHttpClient
import org.michael.kmp.playground.data.repositories.AuthRepositoryImpl
import org.michael.kmp.playground.domain.repositories.AuthRepository
import org.michael.kmp.playground.domain.usecases.LoginUseCase

// Módulo de red
val networkModule = module {
    single { createHttpClient() }

    single<NetworkClient> {
        KtorNetworkClient(
            baseUrl = "http://localhost:3000", // ⬅️ Tu servidor local
            httpClient = get()
        )
    }
}

// Módulo de repositorios
val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}

// Módulo de casos de uso
val useCaseModule = module {
    single { LoginUseCase(get()) }
}

// Lista de todos los módulos
val allModules = listOf(
    networkModule,
    repositoryModule,
    useCaseModule
)