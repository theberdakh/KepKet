package com.theberdakh.kepket.presentation.adapters.vh

import androidx.recyclerview.widget.RecyclerView
import com.theberdakh.kepket.R
import com.theberdakh.kepket.databinding.ItemTableBinding
import com.theberdakh.kepket.presentation.models.TableItem

class TableItemViewHolder(private val binding: ItemTableBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TableItem, onClick: ((TableItem) -> Unit)?) {

        binding.title.text = item.tableNumber.toString()
        if (item.isBusy){
            binding.root.setBackgroundResource(R.drawable.bg_busy_table)
        }
        binding.root.setOnClickListener {
            if (!item.isBusy){
                binding.root.setBackgroundResource(R.drawable.bg_selected_table)
            }
            onClick?.invoke(item)
        }
    }
}