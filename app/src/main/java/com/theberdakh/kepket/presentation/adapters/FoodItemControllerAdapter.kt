package com.theberdakh.kepket.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.theberdakh.kepket.databinding.ItemFoodControllerBinding
import com.theberdakh.kepket.presentation.adapters.vh.FoodItemControllerViewHolder
import com.theberdakh.kepket.presentation.models.FoodItem
import com.theberdakh.kepket.presentation.models.FoodItemDiffUtilCallback

class FoodItemControllerAdapter :
    ListAdapter<FoodItem, FoodItemControllerViewHolder>(FoodItemDiffUtilCallback) {

    private var onFoodItemClickListener: ((FoodItem) -> Unit)? = null
    fun setOnFoodItemClickListener(block: ((FoodItem) -> Unit)) {
        onFoodItemClickListener = block
    }

    private var onIncreaseClickListener: ((FoodItem) -> Unit)? = null
    fun setOnIncreaseClickListener(block: ((FoodItem) -> Unit)) {
        onIncreaseClickListener = block
    }

    private var onDecreaseClickListener: ((FoodItem) -> Unit)? = null
    fun setOnDecreaseClickListener(block: ((FoodItem) -> Unit)) {
        onDecreaseClickListener = block
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoodItemControllerViewHolder {
        return FoodItemControllerViewHolder(
            ItemFoodControllerBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FoodItemControllerViewHolder, position: Int) =
        holder.bind(
            getItem(position),
            onClick = onFoodItemClickListener,
            onIncrease = onIncreaseClickListener,
            onDecrease = onDecreaseClickListener
        )
}