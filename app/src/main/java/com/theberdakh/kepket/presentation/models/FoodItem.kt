package com.theberdakh.kepket.presentation.models

import android.os.Parcel
import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.theberdakh.kepket.data.remote.models.order.OrderDish
import com.theberdakh.kepket.data.remote.models.order.OrderItem
import kotlinx.parcelize.Parcelize


@Parcelize
data class FoodItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val category: String,
    val image: String,
    var quantity: Int = QUANTITY_ZERO
) : Parcelable {
    companion object {
        const val QUANTITY_ZERO = 0
    }
}

object FoodItemDiffUtilCallback : DiffUtil.ItemCallback<FoodItem>() {
    override fun areItemsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
        return oldItem.id == newItem.id && oldItem.image == newItem.image && oldItem.description == newItem.description && oldItem.price == newItem.price
    }

    override fun areContentsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
        return oldItem == newItem
    }
}

fun FoodItem.toOrderItem(): OrderItem {
    return OrderItem(
        dish = OrderDish(id = this.id, name = this.name, price = this.price),
        quantity = this.quantity
    )
}