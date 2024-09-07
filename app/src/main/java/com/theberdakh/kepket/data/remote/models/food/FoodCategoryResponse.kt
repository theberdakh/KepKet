package com.theberdakh.kepket.data.remote.models.food

import com.google.gson.annotations.SerializedName

data class FoodCategoryResponse(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val restaurantId: String,
    @SerializedName("__v")
    val v: Int
)
