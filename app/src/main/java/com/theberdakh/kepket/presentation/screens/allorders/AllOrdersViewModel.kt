package com.theberdakh.kepket.presentation.screens.allorders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.kepket.data.local.LocalPreferences
import com.theberdakh.kepket.data.remote.models.ResultModel
import com.theberdakh.kepket.data.remote.models.Status
import com.theberdakh.kepket.data.remote.models.errorMessage
import com.theberdakh.kepket.data.remote.models.notifications.toOrderItem
import com.theberdakh.kepket.data.remote.socket.IOSocketService
import com.theberdakh.kepket.data.repository.KepKetRepository
import com.theberdakh.kepket.presentation.models.OrderItem
import com.theberdakh.kepket.presentation.models.state.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class AllOrdersViewModel(
    private val kepKetRepository: KepKetRepository,
    private val socketService: IOSocketService,
    private val localPreferences: LocalPreferences,
) : ViewModel(), KoinComponent {


    private val _socketMessageFlow = MutableStateFlow<String?>(null)
    internal val socketMessageFlow = _socketMessageFlow.asStateFlow()
    fun connect() {
        viewModelScope.launch {
            socketService.connect()
        }
    }

    private val _waiterOrdersState = MutableStateFlow(NetworkState<List<OrderItem>>())
    internal val waiterOrdersState: StateFlow<NetworkState<List<OrderItem>>> =
        _waiterOrdersState.asStateFlow()

    fun getWaiterOrders(waiterId: String = localPreferences.getUserInfo().id) =
        viewModelScope.launch {
            Log.d("Orders", "$waiterId")
            Log.d("Token", "${localPreferences.getToken()}")
            kepKetRepository.getWaiterOrders(waiterId)
                .onStart { _waiterOrdersState.emit(NetworkState(isLoading = true)) }
                .catch { e ->
                    _waiterOrdersState.emit(
                        NetworkState(
                            isLoading = false,
                            result = ResultModel.error(e)
                        )
                    )
                }.collect { orderResponse ->
                    when (orderResponse.status) {
                        Status.SUCCESS -> {
                            Log.d("Order", "Success")
                            val orderItems = mutableListOf<OrderItem>()
                            orderResponse.data?.forEach { order ->
                                val orderItem = order.toOrderItem()
                                orderItems.add(orderItem)
                            }

                            if (orderResponse.data != null){
                                println(orderResponse.data)
                            }

                            _waiterOrdersState.value = NetworkState(
                                isLoading = false,
                                ResultModel.success(orderItems)
                            )
                        }

                        Status.ERROR -> {
                            Log.d("Order", "Error: ${orderResponse.errorThrowable?.errorMessage}")
                            _waiterOrdersState.value = NetworkState(
                                isLoading = false,
                                orderResponse.errorThrowable?.let { ResultModel.error(it) }
                            )
                        }
                    }
                }
        }
}
