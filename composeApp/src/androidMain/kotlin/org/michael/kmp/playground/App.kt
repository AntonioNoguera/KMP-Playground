package org.michael.kmp.playground

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.michael.kmp.playground.di.androidModule
import org.michael.kmp.playground.placeholder.di.allModules

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(allModules + androidModule)
        }
    }
}