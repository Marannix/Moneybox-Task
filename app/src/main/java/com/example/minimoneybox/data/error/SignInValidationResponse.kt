package com.example.minimoneybox.data.error

import com.google.gson.annotations.SerializedName

data class SignInValidationResponse(
    @SerializedName("Name")
    val name: String
)