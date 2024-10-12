package com.theberdakh.kepket.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.theberdakh.kepket.databinding.ItemChipBinding
import com.theberdakh.kepket.presentation.adapters.vh.ChipItemViewHolder
import com.theberdakh.kepket.presentation.models.ChipItem
import com.theberdakh.kepket.presentation.models.ChipItemDiffUtilCallback

class ChipItemAdapter : ListAdapter<ChipItem, ChipItemViewHolder>(ChipItemDiffUtilCallback) {

     private var onChipItemClickListener: ((ChipItem) -> Unit)? = null
         fun setOnChipItemClickListener(block: ((ChipItem) -> Unit)) {
             onChipItemClickListener = block
         }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipItemViewHolder {
        return ChipItemViewHolder(
            ItemChipBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChipItemViewHolder, position: Int) {
        holder.bind(getItem(position), onChipItemClickListener)
    }

}
