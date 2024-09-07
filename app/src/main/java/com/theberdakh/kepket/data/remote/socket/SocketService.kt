package com.theberdakh.kepket.data.remote.socket

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

class SocketService(private val serverAddress: String, private val serverPort: Int) {

    private var socket: Socket? = null
    private var reader: BufferedReader? = null
    private var writer: PrintWriter? = null

    suspend fun connect() {
        withContext(Dispatchers.IO) {
            try {
                socket = Socket(serverAddress, serverPort)
                reader = BufferedReader(InputStreamReader(socket?.getInputStream()))
                writer = PrintWriter(OutputStreamWriter(socket?.getOutputStream()), true)
                Log.d("SocketService", "Connected to $serverAddress:$serverPort")
            } catch (e: Exception) {
                Log.e("SocketService", "Connection failed", e)
            }
        }
    }

    suspend fun sendMessage(message: String) {
        withContext(Dispatchers.IO) {
            try {
                writer?.println(message)
                Log.d("SocketService", "Sent: $message")
            } catch (e: Exception) {
                Log.e("SocketService", "Send failed", e)
            }
        }
    }

    suspend fun receiveMessage(): String? {
        return withContext(Dispatchers.IO) {
            try {
                reader?.readLine()
            } catch (e: Exception) {
                Log.e("SocketService", "Receive failed", e)
                null
            }
        }
    }

    fun close() {
        try {
            reader?.close()
            writer?.close()
            socket?.close()
        } catch (e: Exception) {
            Log.e("SocketService", "Close failed", e)
        }
    }
}

