package com.example.minimoneybox

import com.google.gson.annotations.SerializedName

class InvestorProducts(
    @SerializedName("TotalPlanValue")
    val totalPlanValue: Double,
    @SerializedName("ProductResponses")
    val productResponses: List<ProductResponses>
)