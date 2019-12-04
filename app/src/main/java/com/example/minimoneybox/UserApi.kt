package com.example.minimoneybox

import io.reactivex.Single
import retrofit2.http.*

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