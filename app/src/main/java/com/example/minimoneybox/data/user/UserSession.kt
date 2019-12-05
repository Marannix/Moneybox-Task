package com.example.minimoneybox.data.user

import com.google.gson.annotations.SerializedName

data class UserSession(
    @SerializedName("BearerToken")
    val bearerToken: String
)