package com.theberdakh.kepket.presentation.adapters.vh

import androidx.recyclerview.widget.RecyclerView
import com.theberdakh.kepket.databinding.ItemOrderBinding
import com.theberdakh.kepket.presentation.models.OrderItem

class OrderItemViewHolder(private val binding: ItemOrderBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(orderItem: OrderItem){
        binding.title.text = orderItem.meals.joinToString(separator = ", ")
        binding.subtitle.text = orderItem.status
        binding.tableNumber.text = orderItem.tableNumber.toString()
        binding.price.text = orderItem.totalPrice.toString()
    }
}
