package com.theberdakh.kepket.data.remote.models.notifications

import com.theberdakh.kepket.data.remote.models.order.CreateOrderResponse

data class WaiterNotificationResponse(
    val pending: List<WaiterNotificationOrder>
)
