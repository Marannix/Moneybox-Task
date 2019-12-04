package com.example.minimoneybox.state

import com.example.minimoneybox.UserResponse

sealed class UserDataState {
    data class Success(val user: UserResponse) : UserDataState()

    //TODO: Use this to display error message if incorrect Username or Password
    data class Error(val errorMessage: String?) : UserDataState()
}