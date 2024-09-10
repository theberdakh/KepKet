package com.theberdakh.kepket.presentation.adapters.vh

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.theberdakh.kepket.R
import com.theberdakh.kepket.databinding.ItemFoodAddBinding
import com.theberdakh.kepket.presentation.models.FoodItem

class FoodItemViewHolder(private val binding: ItemFoodAddBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FoodItem, onAddClick: ((FoodItem) -> Unit)?) {

        Glide.with(binding.root.context)
            .load(item.image)
            .placeholder(R.drawable.logo)
            .into(binding.foodImage)


        binding.title.text = item.name
        binding.subtitle.text = item.description
        binding.price.text = item.price.toString()

        binding.btnAdd.setOnClickListener {
            if (item.quantity == FoodItem.QUANTITY_ZERO) {
                binding.btnAdd.setImageResource(R.drawable.ic_done)
                item.quantity += 1
            }
            onAddClick?.invoke(item)
        }
    }
}