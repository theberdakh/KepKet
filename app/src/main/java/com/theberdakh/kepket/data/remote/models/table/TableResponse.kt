package com.theberdakh.kepket.data.remote.models.table

import com.google.gson.annotations.SerializedName
import com.theberdakh.kepket.presentation.models.TableItem

data class TableResponse(
    @SerializedName("_id")
    val id: String,
    val tableNumber: Int,
    val capacity: Int,
    val restaurantId: String,
    @SerializedName("__v")
    val v: Int
)

fun TableResponse.toTableItem(): TableItem {
    return TableItem(
        id = this.id,
        tableNumber = this.tableNumber
    )
}
