package com.example.minimoneybox.api

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    /**
     *   Added an interceptor to add the headers necessary for making each request
     */
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("AppId", "3a97b932a9d449c981b595")
                .addHeader("Content-Type", "application/json")
                .addHeader("appVersion", "5.10.0")
                .addHeader("apiVersion", "3.0.0")
                .build()
        )
    }
}