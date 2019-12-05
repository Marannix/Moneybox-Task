package com.example.minimoneybox.data.products

import com.example.minimoneybox.data.products.ProductResponses
import com.google.gson.annotations.SerializedName

class InvestorProducts(
    @SerializedName("TotalPlanValue")
    val totalPlanValue: Double,
    @SerializedName("ProductResponses")
    val productResponses: List<ProductResponses>
)