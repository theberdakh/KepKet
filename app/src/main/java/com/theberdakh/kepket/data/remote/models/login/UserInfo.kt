package com.theberdakh.kepket.data.remote.models.login

import com.google.gson.annotations.SerializedName

/*_id": "66d9b2f2ccfa11f1d4b3581d",
        "username": "Amir",
        "password": "$2b$10$Kgao2.ypA96Ry6wTArwmIuVXBoRo0MRmuadrEGGV0fJN8JyT0SksK",
        "imageWaiter": "https://img.jpg",
        "rating": 5,
        "atWork": true,
        "restaurantId": "66d99e8e297800fa51259fdf",
        "busy": false,
        "__v": 0*/
data class UserInfo(
    @SerializedName("_id")
    val id: String,
    val username: String,
    val password: String,
    val imageWaiter: String,
    val rating: Int,
    val atWork: Boolean,
    val restaurantId: String,
    val busy: Boolean,
    @SerializedName("__v")
    val v: Int
)
