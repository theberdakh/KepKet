package com.theberdakh.kepket.presentation.models

import androidx.recyclerview.widget.DiffUtil

data class TableItem(
    val id: String,
    val tableNumber: Int
)

object TableItemDiffUtilCallback: DiffUtil.ItemCallback<TableItem>(){
  override fun areItemsTheSame(oldItem: TableItem, newItem: TableItem): Boolean {
      return oldItem.id == newItem.id && oldItem.tableNumber == newItem.tableNumber
    }

    override fun areContentsTheSame(oldItem: TableItem, newItem: TableItem): Boolean {
       return oldItem == newItem
    }
}