package com.example.minimoneybox.state

import com.example.minimoneybox.data.products.ProductResponses

sealed class InvestorProductsViewState {
    object Loading : InvestorProductsViewState()
    data class ShowProducts (
        val isa: ProductResponses,
        val lisa: ProductResponses,
        val gia: ProductResponses,
        val totalPlanValue: Double
    ) : InvestorProductsViewState()
    data class ShowError(val errorMessage: Int, val errorCode: Int?) : InvestorProductsViewState()
    data class ShowUnknownError(val errorMessage: String?, val errorCode: Int?) : InvestorProductsViewState()
}