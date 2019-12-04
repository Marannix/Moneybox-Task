package com.example.minimoneybox

import io.reactivex.Single
import retrofit2.http.*

interface UserApi {

    // Need to check if this is the correct response
    @POST("users/login/")
    @FormUrlEncoded
    fun getUser(
        @Field("Email") email: String,
        @Field("Password") password: String,
        @Field("Idfa") idfa: String
    ) : Single<UserResponse>

}