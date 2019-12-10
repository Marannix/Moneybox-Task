package com.example.minimoneybox.state

import com.example.minimoneybox.data.products.InvestedMoneyboxResponse

sealed class InvestedMoneyboxViewState {
    object Loading : InvestedMoneyboxViewState()
    data class ShowUpdatedMoneyBox(val response: InvestedMoneyboxResponse) : InvestedMoneyboxViewState()
    data class ShowAuthError(val errorMessage: Int, val errorCode: Int?) : InvestedMoneyboxViewState()
    data class ShowGenericError(val errorMessage: String?, val errorCode: Int?) : InvestedMoneyboxViewState()
}