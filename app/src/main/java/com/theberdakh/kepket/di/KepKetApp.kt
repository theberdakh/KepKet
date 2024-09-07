package com.theberdakh.kepket.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KepKetApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@KepKetApp)
            modules(preferenceModule, networkModule, repositoryModule, viewModelModule)

        }

    }

}
