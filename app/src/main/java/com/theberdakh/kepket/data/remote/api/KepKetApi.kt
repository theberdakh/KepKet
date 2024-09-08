package com.theberdakh.kepket.data.remote.api

import com.theberdakh.kepket.data.remote.models.category.CategoryResponse
import com.theberdakh.kepket.data.remote.models.login.LoginRequest
import com.theberdakh.kepket.data.remote.models.login.LoginResponse
import com.theberdakh.kepket.data.remote.models.notifications.OrderResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface KepKetApi {

    @POST("api/waiter/login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/category/all/{restaurantId}")
    suspend fun getAllFoodCategory(@Path("restaurantId") restaurantId: String): List<CategoryResponse>

    @GET("api/notifications/{waiterId}")
    suspend fun getWaiterOrders(@Path("waiterId") waiterID: String): List<OrderResponse>
}
