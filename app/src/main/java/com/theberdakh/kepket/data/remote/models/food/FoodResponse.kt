package com.theberdakh.kepket.data.remote.models.food

import com.google.gson.annotations.SerializedName
import com.theberdakh.kepket.presentation.models.FoodItem

data class FoodResponse(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    @SerializedName("category")
    val category: FoodResponseCategory,
    val image: String,
    @SerializedName("restourantId")
    val restaurantId: String,
    val createdAt: String,
    val updatedAt: String,
    @SerializedName("__v")
    val v: Int
)

fun FoodResponse.toFoodItem(): FoodItem {
    return FoodItem(id, name, description, price, category.name, image)
}
