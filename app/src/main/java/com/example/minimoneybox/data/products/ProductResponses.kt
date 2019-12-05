package com.example.minimoneybox.data.products

import com.google.gson.annotations.SerializedName

data class ProductResponses(
    @SerializedName("Id")
    val id : Int,
    @SerializedName("PlanValue")
    val planValue: Double,
    @SerializedName("Moneybox")
    val moneyBox: Double,
    @SerializedName("Product")
    val products: Products
)
