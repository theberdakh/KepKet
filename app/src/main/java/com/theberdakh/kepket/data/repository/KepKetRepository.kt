package com.theberdakh.kepket.data.repository

import com.theberdakh.kepket.data.remote.api.KepKetApi
import com.theberdakh.kepket.data.remote.models.login.LoginRequest
import com.theberdakh.kepket.data.remote.models.ResultModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class KepKetRepository(private val kepKetApi: KepKetApi) {

    fun login(username: String, password: String) = flow {
        val response = kepKetApi.login(LoginRequest(username, password))
        emit(ResultModel.success(response))
    }.catch {
        emit(ResultModel.error(it))
    }

    fun getWaiterOrders(waiterId: String) = flow {
        val response = kepKetApi.getWaiterOrders(waiterId)
        emit(ResultModel.success(response))
    }.catch {
        emit(ResultModel.error(it))
    }

}
