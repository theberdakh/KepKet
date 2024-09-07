package com.theberdakh.kepket.di

import com.theberdakh.kepket.presentation.statemanagers.allorders.AllOrdersViewModel
import com.theberdakh.kepket.presentation.statemanagers.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<LoginViewModel>{
        LoginViewModel(kepKetRepository = get(), localPreferences = get())
    }
    viewModel<AllOrdersViewModel>{
        AllOrdersViewModel(kepKetRepository = get(), localPreferences = get(), socketService = get())
    }
}
