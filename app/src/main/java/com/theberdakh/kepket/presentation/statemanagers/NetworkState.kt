package com.theberdakh.kepket.presentation.statemanagers

import com.theberdakh.kepket.data.remote.models.ResultModel

data class NetworkState<T>(
    val isLoading: Boolean = false,
    val result: ResultModel<T>? = null
)
