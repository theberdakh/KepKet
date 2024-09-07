package com.theberdakh.kepket.data.remote.models.login

data class LoginResponse(
    val data: UserInfo,
    val token: String
)
