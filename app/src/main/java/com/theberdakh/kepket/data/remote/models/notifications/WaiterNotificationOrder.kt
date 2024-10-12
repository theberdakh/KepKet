package com.theberdakh.kepket.data.remote.models.notifications

import com.google.gson.annotations.SerializedName
import com.theberdakh.kepket.data.remote.models.order.OrderWaiter
import com.theberdakh.kepket.presentation.models.OrderItem

data class WaiterNotificationOrder(
    @SerializedName("_id")
    val id: String,
    val waiter: WaiterNotificationWaiter,
    val table: WaiterNotificationTable,
    val totalPrice: Int,
    val meals: List<WaiterNotificationMeal>,
    val status: String,
    val restaurantId: String,
    val createdAt: String,
    val updatedAt: String
)

fun WaiterNotificationOrder.toOrderItem() : OrderItem {
    return OrderItem(
        id = this.id,
        status = this.status,
        totalPrice = this.totalPrice,
        tableNumber = this.table.number,
        meals = this.meals.map {
            it.toMealItem()
        }
    )
}
