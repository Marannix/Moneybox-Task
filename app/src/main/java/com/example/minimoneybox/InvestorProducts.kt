package com.example.minimoneybox

import com.google.gson.annotations.SerializedName

class InvestorProducts(
    @SerializedName("ProductResponses")
    val productResponses: List<ProductResponses>
)