package com.theberdakh.kepket.data.remote.models.notifications

import com.google.gson.annotations.SerializedName
import com.theberdakh.kepket.presentation.models.OrderItem

data class OrderResponse(
    @SerializedName("__v")
    val v: Int,
    @SerializedName("_id")
    val id: String,
    val createdAt: String,
    val meals: List<MealResponse>,
    val restaurantId: String,
    val status: String,
    val table: TableResponse,
    val updatedAt: String,
    val waiter: WaiterResponse
)

fun OrderResponse.toOrderItem(): OrderItem {
    return OrderItem(
        id = this.id,
        meals = this.meals.map { mealResponse -> mealResponse.name },
        status = this.status,
        tableNumber = this.table.number
    )
}
