package com.theberdakh.kepket.presentation.models

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class TableItem(
    val id: String,
    val tableNumber: Int,
    val isBusy: Boolean
): Parcelable

object TableItemDiffUtilCallback: DiffUtil.ItemCallback<TableItem>(){
  override fun areItemsTheSame(oldItem: TableItem, newItem: TableItem): Boolean {
      return oldItem.id == newItem.id && oldItem.tableNumber == newItem.tableNumber
    }

    override fun areContentsTheSame(oldItem: TableItem, newItem: TableItem): Boolean {
       return oldItem == newItem
    }
}