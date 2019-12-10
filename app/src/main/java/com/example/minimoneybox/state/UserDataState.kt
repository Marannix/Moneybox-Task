package com.example.minimoneybox.state

import com.example.minimoneybox.data.user.UserResponse

sealed class UserDataState {
    data class Success(val user: UserResponse) : UserDataState()
    data class Error(val errorMessage: String?) : UserDataState()
}