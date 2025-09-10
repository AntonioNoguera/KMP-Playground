package org.michael.kmp.playground.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.michael.kmp.playground.application.login.PostsViewModel
import org.michael.kmp.playground.firestore.FlagsViewModel

val androidModule = module {
    viewModel { PostsViewModel(get()) }
}
