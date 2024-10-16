package com.theberdakh.kepket.data.remote.api

import com.theberdakh.kepket.data.remote.models.category.CategoryResponse
import com.theberdakh.kepket.data.remote.models.food.FoodResponse
import com.theberdakh.kepket.data.remote.models.login.LoginRequest
import com.theberdakh.kepket.data.remote.models.login.LoginResponse
import com.theberdakh.kepket.data.remote.models.notifications.WaiterNotificationResponse
import com.theberdakh.kepket.data.remote.models.order.CreateOrderRequest
import com.theberdakh.kepket.data.remote.models.order.CreateOrderResponse
import com.theberdakh.kepket.data.remote.models.table.TableResponse
import com.theberdakh.kepket.data.remote.models.table.TableResponseTable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface KepKetApi {

    @POST("api/waiter/login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/category/all/{restaurantId}")
    suspend fun getAllFoodCategory(@Path("restaurantId") restaurantId: String): List<CategoryResponse>

    @GET("api/orders/all/{restaurantId}")
    suspend fun getRestaurantOrders(@Path("restaurantId") restaurantId: String): List<CreateOrderResponse>

    @GET("api/table/all-tables/{restaurantId}")
    suspend fun getAllTables(@Path("restaurantId") restaurantId: String): TableResponse

    @GET("api/dishes/{restaurantId}")
    suspend fun getAllFoods(@Path("restaurantId") restaurantId: String): List<FoodResponse>

    @POST("api/orders/waiter-order")
    suspend fun createOrder(@Body createOrderRequest: CreateOrderRequest): CreateOrderResponse

    @PUT("api/notifications/complate/{orderId}")
    suspend fun completeOrder(@Path("orderId") orderId: String): CreateOrderResponse

    @GET("/api/notifications")
    suspend fun getWaiterNotifications(): WaiterNotificationResponse



}
