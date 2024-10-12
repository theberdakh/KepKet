package com.theberdakh.kepket.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.theberdakh.kepket.databinding.ItemMealBinding
import com.theberdakh.kepket.presentation.adapters.vh.MealItemViewHolder
import com.theberdakh.kepket.presentation.models.MealItem
import com.theberdakh.kepket.presentation.models.MealItemDiffUtilCallback

class MealItemAdapter: ListAdapter<MealItem, MealItemViewHolder>(MealItemDiffUtilCallback) {

     private var onMealItemClickListener: ((MealItem) -> Unit)? = null
         fun setOnMealItemClickListener(block: ((MealItem) -> Unit)) {
             onMealItemClickListener = block
         }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MealItemViewHolder {
        return MealItemViewHolder(ItemMealBinding.inflate(
            LayoutInflater.from(p0.context),
            p0,
            false
        ))
    }

    override fun onBindViewHolder(p0: MealItemViewHolder, p1: Int) = p0.bind(getItem(p1), onMealItemClickListener)

}