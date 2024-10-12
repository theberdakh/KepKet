package com.theberdakh.kepket.data.remote.models.order

private const val ORDER_STATUS_PENDING = "Pending"
private const val NO_PROMO_CODE = ""

data class CreateOrderRequest(
    val restaurantId: String,
    val tableNumber: OrderTable,
    val items: List<CreateOrderItem>,
    val waiter: OrderWaiter,
    val status: String = ORDER_STATUS_PENDING,
    val promoCode: String = NO_PROMO_CODE
)