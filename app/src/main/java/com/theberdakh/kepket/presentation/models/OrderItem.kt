package com.theberdakh.kepket.presentation.models

import androidx.recyclerview.widget.DiffUtil

data class OrderItem(
    val id: String,
    val meals: List<MealItem>,
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
