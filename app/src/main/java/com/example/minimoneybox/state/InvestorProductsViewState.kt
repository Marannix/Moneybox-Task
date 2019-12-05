package com.example.minimoneybox.state

import com.example.minimoneybox.ProductResponses

sealed class InvestorProductsViewState {
    object Loading : InvestorProductsViewState()
    data class ShowProducts(val isa: ProductResponses, val lisa: ProductResponses, val gia: ProductResponses) : InvestorProductsViewState()
//    data class ShowLISA(val lisa: ProductResponses) : InvestorProductsViewState()
//    data class ShowGIA(val gia: ProductResponses) : InvestorProductsViewState()
    data class ShowError(val errorMessage: String?) : InvestorProductsViewState()
}