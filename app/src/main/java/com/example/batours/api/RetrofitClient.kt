package com.example.batours.api

import android.util.Log
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    private val AUTH = Credentials.basic("lokijuhy123123", "secret0123")

    private const val BASE_URL = "https://express-api-modular-template-production.up.railway.app/api/v1/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->

            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .addHeader("Authorization", AUTH)
                .method(original.method(), original.body())

            val request = requestBuilder.build()
            chain.proceed(request)
        }

    val instance: Api by lazy{
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY


        okHttpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .build()

        retrofit.create(Api::class.java)
    }

}