package com.theberdakh.kepket.di

import com.theberdakh.kepket.presentation.screens.allfoods.AllFoodScreenViewModel
import com.theberdakh.kepket.presentation.screens.allorders.AllOrdersViewModel
import com.theberdakh.kepket.presentation.screens.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<LoginViewModel>{
        LoginViewModel(kepKetRepository = get(), localPreferences = get())
    }
    viewModel<AllOrdersViewModel>{
        AllOrdersViewModel(kepKetRepository = get(), localPreferences = get(), socketService = get())
    }
    viewModel<AllFoodScreenViewModel>{
        AllFoodScreenViewModel(kepKetRepository = get(), localPreferences = get())
    }
}
