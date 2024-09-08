package com.theberdakh.kepket.presentation.models

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil

data class ChipItem(
    @DrawableRes val icon: Int? = null,
    val title: String
)

object ChipItemDiffUtilCallback: DiffUtil.ItemCallback<ChipItem>(){
    override fun areItemsTheSame(oldItem: ChipItem, newItem: ChipItem): Boolean {
        return oldItem.icon == newItem.icon && oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: ChipItem, newItem: ChipItem): Boolean {
        return oldItem == newItem
    }

}
