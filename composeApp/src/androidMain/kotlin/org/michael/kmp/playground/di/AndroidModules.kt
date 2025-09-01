package org.michael.kmp.playground.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.michael.kmp.playground.android.presentation.PostViewModel

val androidViewModelModule = module {
    // ViewModels específicos de Android
    viewModel { PostViewModel(get()) }
}