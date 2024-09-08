package com.theberdakh.kepket.data.remote.models.category

import com.google.gson.annotations.SerializedName
import com.theberdakh.kepket.presentation.models.ChipItem

data class CategoryResponse(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val restaurantId: String,
    @SerializedName("__v")
    val v: Int
)

fun CategoryResponse.toChipItem(): ChipItem {
    return ChipItem(title = this.name)
}
