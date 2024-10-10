package com.theberdakh.kepket.presentation.adapters.vh

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.theberdakh.kepket.R
import com.theberdakh.kepket.databinding.ItemFoodControllerBinding
import com.theberdakh.kepket.presentation.models.FoodItem

class FoodItemControllerViewHolder(private val binding: ItemFoodControllerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FoodItem, onClick: ((FoodItem) -> Unit)?,
             onIncrease: ((FoodItem) -> Unit)?,
             onDecrease: ((FoodItem) -> Unit)?) {
        Glide.with(binding.root.context)
            .load(item.image)
            .placeholder(R.drawable.logo)
            .into(binding.foodImage)

        binding.title.text = item.name
        binding.price.text = item.price.toString()
        binding.counter.text = item.quantity.toString()

        binding.increase.setOnClickListener {
            onIncrease?.invoke(item)
        }

        binding.decrease.setOnClickListener {
            onDecrease?.invoke(item)
        }

        binding.root.setOnClickListener {
            onClick?.invoke(item)
        }
    }
}