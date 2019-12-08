package com.example.minimoneybox.state

import com.example.minimoneybox.data.products.ProductResponses

sealed class InvestorProductsDataState {
    data class Success(val investorProducts: List<ProductResponses>) : InvestorProductsDataState()
    // TODO: Maybe an error will occur due to token expiring or invalid?
    data class Error(val errorMessage: Int, val errorCode : Int?) : InvestorProductsDataState()
    data class UnknownError(val errorMessage: String?, val errorCode : Int?) : InvestorProductsDataState()
}