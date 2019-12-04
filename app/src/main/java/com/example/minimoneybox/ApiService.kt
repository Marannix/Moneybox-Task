package com.example.minimoneybox

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

const val BASE_URL = "https://api-test01.moneyboxapp.com"

interface ApiService {

    fun provideRetrofit(): Retrofit {
        val httpClient = OkHttpClient.Builder().addInterceptor(HeaderInterceptor()).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    fun userApi(): UserApi {
        return provideUserApi(provideRetrofit())
    }
}

