package org.michael.kmp.playground

import android.app.Application
import firebaseModule
import flagsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.michael.kmp.playground.auth.di.allModules

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(allModules + flagsModule + firebaseModule)
        }

    }
}