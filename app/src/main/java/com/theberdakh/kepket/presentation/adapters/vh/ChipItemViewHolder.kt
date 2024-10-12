package com.theberdakh.kepket.presentation.adapters.vh

import androidx.recyclerview.widget.RecyclerView
import com.theberdakh.kepket.databinding.ItemChipBinding
import com.theberdakh.kepket.presentation.models.ChipItem
import com.theberdakh.kepket.presentation.models.OrderItem

class ChipItemViewHolder(private val binding: ItemChipBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(chipItem: ChipItem, onClick: ((ChipItem) -> Unit)?){
        binding.title.text = chipItem.title
        binding.root.setOnClickListener {
            onClick?.invoke(chipItem)
        }
    }
}
