package com.example.minimoneybox

import com.google.gson.annotations.SerializedName

data class UserSession(
    @SerializedName("BearerToken")
    val bearerToken: String
)