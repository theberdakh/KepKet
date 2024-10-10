package com.theberdakh.kepket.presentation.screens.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.kepket.presentation.models.FoodItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderScreenViewModel: ViewModel() {

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



}