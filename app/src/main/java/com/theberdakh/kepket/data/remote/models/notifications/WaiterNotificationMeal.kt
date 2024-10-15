package com.theberdakh.kepket.data.remote.models.notifications

import com.google.gson.annotations.SerializedName
import com.theberdakh.kepket.presentation.models.MealItem

data class WaiterNotificationMeal(
    val foodImage: String,
    val foodId: String,
    val foodPrice: Int,
    val foodName: String,
    val quantity: Int,

)

fun WaiterNotificationMeal.toMealItem(): MealItem {
    return MealItem(name = this.foodName, price = this.foodPrice, quantity = this.quantity, foodImage = this.foodImage)
}



