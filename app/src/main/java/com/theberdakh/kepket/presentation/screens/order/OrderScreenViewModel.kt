package com.theberdakh.kepket.presentation.screens.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.kepket.data.local.LocalPreferences
import com.theberdakh.kepket.data.remote.models.ResultModel
import com.theberdakh.kepket.data.remote.models.Status
import com.theberdakh.kepket.data.remote.models.order.CreateOrderRequest
import com.theberdakh.kepket.data.remote.models.order.CreateOrderResponse
import com.theberdakh.kepket.data.remote.models.order.OrderTable
import com.theberdakh.kepket.data.remote.models.order.OrderWaiter
import com.theberdakh.kepket.data.repository.KepKetRepository
import com.theberdakh.kepket.presentation.models.FoodItem
import com.theberdakh.kepket.presentation.models.TableItem
import com.theberdakh.kepket.presentation.models.state.NetworkState
import com.theberdakh.kepket.presentation.models.toOrderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderScreenViewModel(private val localPreferences: LocalPreferences, private val repository: KepKetRepository): ViewModel() {

    private val _allFoods = MutableStateFlow<List<FoodItem>>(emptyList())
    internal val allFoods: StateFlow<List<FoodItem>> = _allFoods.asStateFlow()

    fun addFoods(foods: List<FoodItem>) = viewModelScope.launch {
        _allFoods.emit(foods)
        Log.d("allfoodsstate vm", "${allFoods.value}")
    }

    fun increaseQuantity(foodItem: FoodItem) {
        _allFoods.update { currentList ->
            currentList.map { item ->
                if (item.id == foodItem.id){
                    item.copy(quantity = item.quantity +1)
                } else {
                    item
                }
            }
        }
        getTotalPrice()
    }

    fun decreaseQuantity(foodItem: FoodItem) {
        _allFoods.update { currentList ->
            currentList.mapNotNull { item ->
                if (item.id == foodItem.id) {
                    // Decrease quantity only if it's greater than 1
                    if (item.quantity > 1) {
                        item.copy(quantity = item.quantity - 1)
                    } else null // Remove the item if the quantity reaches 0
                } else {
                    item
                }
            }
        }
        getTotalPrice()
    }


    private val _totalPriceState = MutableStateFlow<Int>(0)
    internal val totalPriceState: StateFlow<Int> = _totalPriceState.asStateFlow()

    fun getTotalPrice() = viewModelScope.launch {
        var totalPrice = 0
        allFoods.onEach {
            if (it.isNotEmpty()){
                for(food in it){
                    totalPrice += (food.price * food.quantity)
                }
            }
            _totalPriceState.emit(totalPrice)
        }.launchIn(viewModelScope)
    }

    private val _createOrderState = MutableStateFlow(NetworkState<CreateOrderResponse>())
    internal val createOrderState: StateFlow<NetworkState<CreateOrderResponse>> = _createOrderState.asStateFlow()
    fun createOrder(table: TableItem) = viewModelScope.launch {

        val request = CreateOrderRequest(
            restaurantId = localPreferences.getUserInfo().restaurantId,
            tableNumber = OrderTable(id = table.id, number = table.tableNumber),
            items = allFoods.value.map { item ->
                item.toOrderItem()
            },
            waiter = OrderWaiter(localPreferences.getUserInfo().id)
        )
        repository.createOrder(request).onStart {
            _createOrderState.value = NetworkState(isLoading = true) }
            .catch { exception ->
                _createOrderState.value = NetworkState(
                    isLoading = false,
                    result = ResultModel.error(exception)
                )
            }.collect { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        _createOrderState.value = NetworkState(
                            isLoading = false,
                            result = result
                        )
                    }
                    Status.ERROR -> {
                        _createOrderState.value = NetworkState(
                            isLoading = false,
                            result = result.errorThrowable?.let { ResultModel.error(it) }
                        )
                    }
                }
            }
    }


}