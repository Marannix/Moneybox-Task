package com.example.minimoneybox.data.products

import com.google.gson.annotations.SerializedName

data class InvestedMoneyboxResponse(
    @SerializedName("Moneybox")
    val moneyBox: String
)