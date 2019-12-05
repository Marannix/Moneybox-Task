package com.example.minimoneybox.api

import com.example.minimoneybox.data.products.InvestorProducts
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header

interface ProductsApi {

    @GET("investorproducts/")
    fun getInvestorProducts(@Header("Authorization") token: String) : Single<InvestorProducts>
}