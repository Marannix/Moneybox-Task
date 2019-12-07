package com.example.minimoneybox.state

import com.example.minimoneybox.data.products.InvestedMoneyboxResponse

sealed class InvestedMoneyboxDataState {
    // This is the new balance
    data class Success(val response: InvestedMoneyboxResponse) : InvestedMoneyboxDataState()
    // TODO: Maybe an error will occur due to token expiring or invalid?
    data class Error(val errorMessage: String?) : InvestedMoneyboxDataState()
}