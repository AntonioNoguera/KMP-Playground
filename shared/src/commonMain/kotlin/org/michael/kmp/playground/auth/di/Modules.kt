package org.michael.kmp.playground.auth.di


import io.ktor.client.HttpClient
import org.koin.dsl.module
import org.michael.kmp.playground.core.network.KtorNetworkClient
import org.michael.kmp.playground.core.network.NetworkClient
import org.michael.kmp.playground.core.network.createHttpClient
import org.michael.kmp.playground.auth.data.repositories.LoginRepositoryImpl
import org.michael.kmp.playground.auth.domain.repositories.AuthRepository
import org.michael.kmp.playground.auth.domain.usecases.LoginUseCase

val networkModule = module {
    single<HttpClient> { createHttpClient() }

    single<NetworkClient> {
        KtorNetworkClient(
            baseUrl = "https://ateneabackend-production.up.railway.app",
            httpClient = get()
        )
    }
}

val repositoryModule = module {
    single<AuthRepository> { LoginRepositoryImpl(get()) }
}

val useCaseModule = module {
    single { LoginUseCase(get()) }
}

val allModules = listOf(networkModule, repositoryModule, useCaseModule)