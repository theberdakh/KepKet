package com.theberdakh.kepket.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.theberdakh.kepket.databinding.ItemOrderBinding
import com.theberdakh.kepket.presentation.adapters.vh.OrderItemViewHolder
import com.theberdakh.kepket.presentation.models.OrderItem
import com.theberdakh.kepket.presentation.models.OrderItemDiffUtilCallback

class OrderItemAdapter: ListAdapter<OrderItem, OrderItemViewHolder>(OrderItemDiffUtilCallback) {

     private var onOrderItemClickListener: ((OrderItem) -> Unit)? = null
         fun setOnOrderItemClickListener(block: ((OrderItem) -> Unit)) {
             onOrderItemClickListener = block
         }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        return OrderItemViewHolder(ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) = holder.bind(getItem(position), onOrderItemClickListener)
}
