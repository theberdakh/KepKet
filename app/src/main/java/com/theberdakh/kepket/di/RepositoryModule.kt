package com.theberdakh.kepket.di

import com.theberdakh.kepket.data.repository.KepKetRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<KepKetRepository> {
        KepKetRepository(kepKetApi = get())
    }
}
