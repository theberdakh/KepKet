package com.theberdakh.kepket.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.theberdakh.kepket.databinding.ItemTableBinding
import com.theberdakh.kepket.presentation.adapters.vh.TableItemViewHolder
import com.theberdakh.kepket.presentation.models.TableItem
import com.theberdakh.kepket.presentation.models.TableItemDiffUtilCallback

class TableItemAdapter : ListAdapter<TableItem, TableItemViewHolder>(TableItemDiffUtilCallback) {

    private var onTableItemClickListener: ((TableItem) -> Unit)? = null
    fun setOnTableItemClickListener(block: ((TableItem) -> Unit)) {
        onTableItemClickListener = block
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableItemViewHolder {
        return TableItemViewHolder(
            ItemTableBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TableItemViewHolder, position: Int) =
        holder.bind(getItem(position), onTableItemClickListener)
}