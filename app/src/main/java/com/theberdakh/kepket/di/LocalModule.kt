package com.theberdakh.kepket.di

import android.content.Context
import android.content.SharedPreferences
import com.theberdakh.kepket.data.local.LocalPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val preferenceModule = module {
    single<LocalPreferences> {
        LocalPreferences(get())
    }
    single<SharedPreferences>
    { androidContext().getSharedPreferences("pref", Context.MODE_PRIVATE) }
}


