package com.theberdakh.kepket.data.remote.socket

import android.util.Log
import com.theberdakh.kepket.data.local.LocalPreferences
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject


class IOSocketService(private val localPreferences: LocalPreferences) {

    private val socket: Socket by lazy { IO.socket("https://kep-ket-api.vercel.app") }

    fun connect() {
        try {
            socket.connect()

            // Serverga ulanilganda
            socket.on(Socket.EVENT_CONNECT) {
                Log.d("SocketIO", "Serverga ulandik")

                // Ofitsiant ID sini serverga yuborish
                val waiterId = localPreferences.getUserInfo().id // Bu yerda haqiqiy ID ni o'rnating
                Log.d("WaiterID", waiterId)
                socket.emit("waiter_connected", waiterId)
            }

            // Tayyor bo'lgan taom haqida bildirishnoma olish
            socket.on("get_notification") { args ->
                val notifications = args[0] as JSONArray
                for (i in 0 until notifications.length()) {
                    val notification = notifications.getJSONObject(i)
                    Log.d("SocketIO", "Yangi bildirishnoma: $notification")
                }
            }

            // Serverdan xabar olish
            socket.on("order_response") { args ->
                val response = args[0] as JSONObject
                Log.d("SocketIO", "Buyurtma javobi: $response")
            }

            // Xatoliklarni ushlash
            socket.on(Socket.EVENT_CONNECT_ERROR) {
                Log.e("SocketIO", "Serverga ulanishda xatolik")
            }

            socket.on(Socket.EVENT_DISCONNECT) {
                Log.d("SocketIO", "Serverdan uzildi")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }




}
