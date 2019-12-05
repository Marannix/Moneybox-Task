package com.example.minimoneybox

import com.google.gson.annotations.SerializedName

data class Products(
    val id: Int,
    @SerializedName("FriendlyName")
    val friendlyName: String
)
