package com.example.minimoneybox

import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {

    // Need to check if this is the correct response
    @POST("users/login")
    fun getUser(
        @Query("Email") email: Int,
        @Query("Password") password: String,
        @Query("Idfa") idfa: String
    )

}