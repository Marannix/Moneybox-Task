package com.example.minimoneybox.state

import com.example.minimoneybox.UserResponse

sealed class UserViewState {
    object Loading : UserViewState()
    data class ShowUser(val user: UserResponse) : UserViewState()
    data class ShowError(val errorMessage: String?) : UserViewState()
}