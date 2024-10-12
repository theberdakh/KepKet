package com.theberdakh.kepket.presentation.screens.allorders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.kepket.data.local.LocalPreferences
import com.theberdakh.kepket.data.remote.models.ResultModel
import com.theberdakh.kepket.data.remote.models.Status
import com.theberdakh.kepket.data.remote.models.notifications.toOrderItem
import com.theberdakh.kepket.data.remote.models.order.toOrderItem
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

    private val _waiterNotificationsState = MutableStateFlow(NetworkState<List<OrderItem>>())
    internal val waiterNotificationsState: StateFlow<NetworkState<List<OrderItem>>> = _waiterNotificationsState.asStateFlow()

    fun getWaiterNotifications() = viewModelScope.launch {
        kepKetRepository.getWaiterNotifications()
            .onStart { _waiterNotificationsState.emit(NetworkState(isLoading = true)) }
            .catch { e ->
                _waiterNotificationsState.emit(
                    NetworkState(
                        isLoading = false,
                        result = ResultModel.error(e)
                    )
                )
            }.collect { orderResponse ->
                when (orderResponse.status) {
                    Status.SUCCESS -> {
                        val orderItems = mutableListOf<OrderItem>()
                        orderResponse.data?.pending?.forEach { order ->
                            val orderItem = order.toOrderItem()
                            orderItems.add(orderItem)
                        }
                        if (orderResponse.data != null){
                            println(orderResponse.data)
                        }
                        _waiterNotificationsState.value = NetworkState(
                            isLoading = false,
                            ResultModel.success(orderItems)
                        )
                    }

                    Status.ERROR -> {
                        _waiterNotificationsState.value = NetworkState(
                            isLoading = false,
                            orderResponse.errorThrowable?.let { ResultModel.error(it) }
                        )
                    }
                }
            }
    }


    private val _restaurantOrdersState = MutableStateFlow(NetworkState<List<OrderItem>>())
    internal val restaurantOrdersState: StateFlow<NetworkState<List<OrderItem>>> =
        _restaurantOrdersState.asStateFlow()

    fun getRestaurantOrders(restaurantId: String = localPreferences.getUserInfo().restaurantId) =
        viewModelScope.launch {
            kepKetRepository.getRestaurantOrders(restaurantId)
                .onStart { _restaurantOrdersState.emit(NetworkState(isLoading = true)) }
                .catch { e ->
                    _restaurantOrdersState.emit(
                        NetworkState(
                            isLoading = false,
                            result = ResultModel.error(e)
                        )
                    )
                }.collect { orderResponse ->
                    when (orderResponse.status) {
                        Status.SUCCESS -> {
                            val orderItems = mutableListOf<OrderItem>()
                            orderResponse.data?.forEach { order ->
                                val orderItem = order.toOrderItem()
                                orderItems.add(orderItem)
                            }
                            if (orderResponse.data != null){
                                println(orderResponse.data)
                            }
                            _restaurantOrdersState.value = NetworkState(
                                isLoading = false,
                                ResultModel.success(orderItems)
                            )
                        }

                        Status.ERROR -> {
                            _restaurantOrdersState.value = NetworkState(
                                isLoading = false,
                                orderResponse.errorThrowable?.let { ResultModel.error(it) }
                            )
                        }
                    }
                }
        }
}
