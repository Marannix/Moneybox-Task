package com.example.minimoneybox.state

import com.example.minimoneybox.data.products.InvestedMoneyboxResponse

sealed class InvestedMoneyboxViewState {
    object Loading : InvestedMoneyboxViewState()
    data class ShowUpdatedMoneyBox(val response: InvestedMoneyboxResponse) : InvestedMoneyboxViewState()
    data class ShowError(val errorMessage: String?) : InvestedMoneyboxViewState()
}