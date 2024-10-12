package com.theberdakh.kepket.presentation.adapters.vh

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.theberdakh.kepket.R
import com.theberdakh.kepket.databinding.ItemMealBinding
import com.theberdakh.kepket.presentation.models.MealItem

class MealItemViewHolder(private val binding: ItemMealBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MealItem, onMealItemClick: ((MealItem) -> Unit)?) {

        Glide.with(binding.root.context)
            .load(item.foodImage)
            .placeholder(R.drawable.logo)
            .into(binding.foodImage)

        binding.title.text = binding.root.context.getString(R.string.template_name_and_quantity, item.name, item.quantity)
        binding.price.text = item.price.toString()

        binding.root.setOnClickListener {
            onMealItemClick?.invoke(item)
        }

    }
}