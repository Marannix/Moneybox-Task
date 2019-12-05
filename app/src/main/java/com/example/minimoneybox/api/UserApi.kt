package com.example.minimoneybox.api

import com.example.minimoneybox.UserResponse
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApi {

    // Logs user in
    @POST("users/login/")
    @FormUrlEncoded
    fun logInUser(
        @Field("Email") email: String,
        @Field("Password") password: String,
        @Field("Idfa") idfa: String
    ) : Single<UserResponse>

}