package com.theberdakh.kepket.data.repository

import android.util.Log
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
        Log.d("Order", "Response: $response" )
        emit(ResultModel.success(response))
    }.catch {
        Log.d("Order error", "Response: $it" )
        emit(ResultModel.error(it))
    }

    fun getAllCategories(restaurantId: String) = flow {
        val response = kepKetApi.getAllFoodCategory(restaurantId)
        emit(ResultModel.success(response))
    }.catch {
        emit(ResultModel.error(it))
    }

    fun getAllTables(restaurantId: String) = flow {
        val response = kepKetApi.getAllTables(restaurantId)
        emit(ResultModel.success(response))
    }.catch {
        emit(ResultModel.error(it))
    }

}
