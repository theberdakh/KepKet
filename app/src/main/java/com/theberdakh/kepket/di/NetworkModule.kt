package com.theberdakh.kepket.di

import com.theberdakh.kepket.data.local.LocalPreferences
import com.theberdakh.kepket.data.remote.api.KepKetApi
import com.theberdakh.kepket.data.remote.socket.IOSocketService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {
    single { provideRetrofit(get()) }
    single<KepKetApi>{
        get<Retrofit>().create(KepKetApi::class.java)
    }
   // single { SocketService("kep-ket-api.vercel.app", 12345) } // Use the appropriate port
    single { IOSocketService(localPreferences = get()) }
}

fun provideRetrofit(localPreferences: LocalPreferences): Retrofit {
    val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
        HttpLoggingInterceptor.Level.BODY
    )

    val tokenInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = localPreferences.getToken()

        val newRequest = if (token.isNotEmpty()) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        chain.proceed(newRequest)
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(tokenInterceptor)
        .build()

        return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://kep-ket-api.vercel.app")
        .client(client)
        .build()
}


