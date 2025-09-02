package org.michael.kmp.playground.placeholder.di


import io.ktor.client.HttpClient
import org.koin.dsl.module
import org.michael.kmp.playground.core.network.KtorNetworkClient
import org.michael.kmp.playground.core.network.NetworkClient
import org.michael.kmp.playground.core.network.createHttpClient
import org.michael.kmp.playground.placeholder.data.repositories.PostRepositoryImpl
import org.michael.kmp.playground.placeholder.domain.repositories.PostRepository
import org.michael.kmp.playground.placeholder.domain.usecases.GetPostByIdUseCase

val networkModule = module {
    single<HttpClient> { createHttpClient() }

    single<NetworkClient> {
        KtorNetworkClient(
            baseUrl = "https://jsonplaceholder.typicode.com",
            httpClient = get()
        )
    }
}

val repositoryModule = module {
    single<PostRepository> { PostRepositoryImpl(get()) }
}

val useCaseModule = module {
    single { GetPostByIdUseCase(get()) }
}

val allModules = listOf(networkModule, repositoryModule, useCaseModule)