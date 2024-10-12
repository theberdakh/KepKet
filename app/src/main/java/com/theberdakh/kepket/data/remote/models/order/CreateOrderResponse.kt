package com.theberdakh.kepket.data.remote.models.order

import com.google.gson.annotations.SerializedName
import com.theberdakh.kepket.presentation.models.OrderItem
/*
* "waiter": {
                "id": "6706b0501ce7e66de569f5a6",
                "name": "Amir"
            },
            "_id": "670a181dbc47c0bb1bf6b0af",
            "table": {
                "id": "6706bf42087ad4f5127616c5",
                "number": 10
            },
            "totalPrice": 134000,
            "meals": [
                {
                    "name": "Pepsi",
                    "id": "66f290c4911f7dcdc7282809"
                },
                {
                    "name": "Bison Steak",
                    "id": "66df5d5e8e0e073a9c4ce552"
                },
                {
                    "name": "Duo Beef Burger",
                    "id": "66deca2fae530ec18b007577"
                },
                {
                    "name": "Capricciosa",
                    "id": "66decaa0ae530ec18b007583"
                }
            ],
            "status": "Pending",
            "restaurantId": "66dec1fc25c5865fd12bbb0e",
            "createdAt": "2024-10-12T06:33:01.301Z",
            "updatedAt": "2024-10-12T06:33:01.301Z",
            "__v": 0
        }*/
data class CreateOrderResponse(
    val restaurantId: String,
    val tableNumber: OrderTable,
    val items: List<CreateOrderItem>,
    val totalPrice: Int,
    val status: String,
    val promoCode: String?,
    val who: String,
    val waiter: OrderWaiter,
    val payment: Boolean,
    @SerializedName("_id")
    val id: String
)

fun CreateOrderResponse.toOrderItem(): OrderItem {
   return OrderItem(
       id = this.id,
       meals = this.items.map {
           it.toMealItem()
       },
       status = this.status,
       totalPrice = this.totalPrice,
       tableNumber = this.tableNumber.number
   )
}
