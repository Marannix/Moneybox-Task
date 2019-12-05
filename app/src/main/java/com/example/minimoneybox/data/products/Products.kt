package com.example.minimoneybox.data.products

import com.google.gson.annotations.SerializedName

data class Products(
    val id: Int,
    @SerializedName("FriendlyName")
    val friendlyName: String
)
