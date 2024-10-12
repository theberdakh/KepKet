package com.theberdakh.kepket.presentation.screens.complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.kepket.data.remote.models.ResultModel
import com.theberdakh.kepket.data.remote.models.Status
import com.theberdakh.kepket.data.remote.models.order.CreateOrderResponse
import com.theberdakh.kepket.data.repository.KepKetRepository
import com.theberdakh.kepket.presentation.models.state.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CompleteScreenViewModel(private val repository: KepKetRepository): ViewModel() {

    private val _completeOrderState = MutableStateFlow(NetworkState<CreateOrderResponse>())
    internal val completeOrderState: StateFlow<NetworkState<CreateOrderResponse>> = _completeOrderState.asStateFlow()

    fun completeOrder(orderId: String) = viewModelScope.launch {
        repository.completeOrder(orderId).onStart {
            _completeOrderState.value = NetworkState(isLoading = true)
        }.catch { error ->
            _completeOrderState.value = NetworkState(isLoading = false, result = ResultModel.error(error))
        }.collect { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    _completeOrderState.value = NetworkState(
                        isLoading = false,
                        result = result
                    )
                }
                Status.ERROR -> {
                    _completeOrderState.value = NetworkState(
                        isLoading = false,
                        result = result.errorThrowable?.let { ResultModel.error(it) }
                    )
                }
            }
        }
    }
}