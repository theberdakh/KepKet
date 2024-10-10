package com.theberdakh.kepket.data.remote.models.order

import com.google.gson.annotations.SerializedName

data class CreateOrderResponse(
    val restaurantId: String,
    val tableNumber: OrderTable,
    val items: List<OrderItem>,
    val totalPrice: Int,
    val status: String,
    val promoCode: String?,
    val who: String,
    val waiter: OrderWaiter,
    val payment: Boolean,
    @SerializedName("_id")
    val id: String
)
