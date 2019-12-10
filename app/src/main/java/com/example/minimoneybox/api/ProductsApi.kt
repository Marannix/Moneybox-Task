package com.example.minimoneybox.api

import com.example.minimoneybox.data.products.InvestedMoneyboxResponse
import com.example.minimoneybox.data.products.InvestorProductsResponse
import io.reactivex.Single
import retrofit2.http.*

interface ProductsApi {

    @GET("investorproducts/")
    fun getInvestorProducts(@Header("Authorization") token: String): Single<InvestorProductsResponse>

    @POST("oneoffpayments/")
    @FormUrlEncoded
    fun makePayment(
        @Header("Authorization") token: String,
        @Field("Amount") amount: Int,
        @Field("InvestorProductId") id: Int
    ): Single<InvestedMoneyboxResponse>
}