package com.theberdakh.kepket.presentation.models

import androidx.recyclerview.widget.DiffUtil
import com.theberdakh.kepket.data.remote.models.notifications.MealResponse

/*
* data class OrderResponse(
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
)*/
data class OrderItem(
    val id: String,
    val meals: List<String>,
    val status: String,
    val totalPrice: Int,
    val tableNumber: Int
)

object OrderItemDiffUtilCallback: DiffUtil.ItemCallback<OrderItem>(){
    override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
        return oldItem.id == newItem.id && oldItem.meals == newItem.meals && oldItem.status == newItem.status && oldItem.tableNumber == newItem.tableNumber && oldItem.totalPrice == newItem.totalPrice
    }

    override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
       return oldItem == newItem
    }

}
