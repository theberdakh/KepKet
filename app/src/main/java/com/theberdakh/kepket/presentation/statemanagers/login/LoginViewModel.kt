package com.theberdakh.kepket.presentation.statemanagers.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.kepket.data.local.LocalPreferences
import com.theberdakh.kepket.data.remote.models.ResultModel
import com.theberdakh.kepket.data.remote.models.Status
import com.theberdakh.kepket.data.remote.models.errorMessage
import com.theberdakh.kepket.data.remote.models.login.LoginResponse
import com.theberdakh.kepket.data.repository.KepKetRepository
import com.theberdakh.kepket.presentation.statemanagers.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

class LoginViewModel(
    private val kepKetRepository: KepKetRepository,
    private val localPreferences: LocalPreferences
) : ViewModel() {

    private val _loginState = MutableStateFlow(NetworkState<LoginResponse>())
    internal val loginState: StateFlow<NetworkState<LoginResponse>> = _loginState.asStateFlow()

    fun login(username: String, password: String) = viewModelScope.launch {
        kepKetRepository.login(username, password)
            .onStart { _loginState.value = NetworkState(isLoading = true) }
            .catch { exception ->
                _loginState.value = NetworkState(
                    isLoading = false,
                    result = ResultModel.error(exception)
                )
            }.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        _loginState.value = NetworkState(
                            isLoading = false,
                            result = result
                        )
                        result.data?.let {
                            localPreferences.saveToken(it.token)
                            localPreferences.saveUserInfo(it.data)
                            localPreferences.setLoginStatus(isLoggedIn = true)
                        }
                    }
                    Status.ERROR -> {
                        _loginState.value = NetworkState(
                            isLoading = false,
                            result = result.errorThrowable?.let { ResultModel.error(it) }
                        )
                    }
                }
            }
    }


}
