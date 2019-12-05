package com.example.minimoneybox.data.user

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("User")
    val user: User,
    @SerializedName("Session")
    val session: UserSession

)
