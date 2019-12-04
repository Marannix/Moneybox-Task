package com.example.minimoneybox

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("FirstName")
    val firstName: String
)