package com.theberdakh.kepket.presentation.models

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class MealItem(
    val name: String,
    val price: Int,
    val quantity: Int,
    val foodImage: String = ""
): Parcelable

object MealItemDiffUtilCallback: DiffUtil.ItemCallback<MealItem>(){
  override fun areItemsTheSame(oldItem: MealItem, newItem: MealItem): Boolean {
      return oldItem.name == newItem.name && oldItem.price == newItem.price && oldItem.quantity == newItem.quantity
    }

    override fun areContentsTheSame(oldItem: MealItem, newItem: MealItem): Boolean {
       return oldItem == newItem
    }
}
