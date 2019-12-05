package com.example.minimoneybox.state

import com.example.minimoneybox.data.products.InvestorProducts

sealed class InvestorProductsDataState {
    data class Success(val investorProducts: InvestorProducts) : InvestorProductsDataState()
    // TODO: Maybe an error will occur due to token expiring or invalid?
    data class Error(val errorMessage: String?) : InvestorProductsDataState()
}