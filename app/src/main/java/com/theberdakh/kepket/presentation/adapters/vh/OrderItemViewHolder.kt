package com.theberdakh.kepket.presentation.adapters.vh

import androidx.recyclerview.widget.RecyclerView
import com.theberdakh.kepket.databinding.ItemOrderBinding
import com.theberdakh.kepket.presentation.models.FoodItem
import com.theberdakh.kepket.presentation.models.OrderItem

class OrderItemViewHolder(private val binding: ItemOrderBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(orderItem: OrderItem, onAddClick: ((OrderItem) -> Unit)?){
        binding.title.text = orderItem.meals.joinToString(separator = ", ") {
            it.name
        }
        binding.subtitle.text = orderItem.status
        binding.tableNumber.text = orderItem.tableNumber.toString()
        binding.price.text = orderItem.totalPrice.toString()

        binding.root.setOnClickListener {
            onAddClick?.invoke(orderItem)
        }

    }
}
