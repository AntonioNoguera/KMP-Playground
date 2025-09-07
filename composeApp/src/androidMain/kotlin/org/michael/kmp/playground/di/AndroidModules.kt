package org.michael.kmp.playground.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.michael.kmp.playground.PostsViewModel

val androidModule = module {
    viewModel { PostsViewModel(get(),get()) }
}
