package com.example.githubapiapp

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_ULR = "https://api.github.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer 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")
                .build()
            it.proceed(request)
            //헤더 추가
        }
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_ULR)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


}