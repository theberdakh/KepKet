package com.theberdakh.kepket.data.remote.models.order

import com.theberdakh.kepket.presentation.models.FoodItem
import com.theberdakh.kepket.presentation.models.MealItem

data class CreateOrderItem(
    val dish: OrderDish,
    val quantity: Int
)

fun CreateOrderItem.toMealItem(): MealItem {
    return MealItem(
        name = this.dish.name,
        price = this.dish.price,
        quantity = this.quantity,

    )
}

