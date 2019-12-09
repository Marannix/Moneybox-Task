package com.example.minimoneybox.data.error

import com.google.gson.annotations.SerializedName

data class SignInErrorResponse(
    @SerializedName("Name")
    val name: String?,
    @SerializedName("Message")
    val message : String?
)