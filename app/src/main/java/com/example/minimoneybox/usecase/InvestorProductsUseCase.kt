package com.example.minimoneybox.usecase

import com.example.minimoneybox.repository.ProductsRepository
import com.example.minimoneybox.state.InvestedMoneyboxDataState
import com.example.minimoneybox.state.InvestorProductsDataState
import io.reactivex.Observable
import javax.inject.Inject

class InvestorProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {

    fun getInvestorProductsDataState(token: String): Observable<InvestorProductsDataState> {
        return productsRepository.getInvestorProducts(token)
            .map<InvestorProductsDataState> { products ->
                InvestorProductsDataState.Success(products)
            }.doOnError { error ->
                InvestorProductsDataState.Error(error.message)
            }
    }

    fun makeOneOffPayment(
        token: String,
        amount: Int,
        investorProductId: Int
    ): Observable<InvestedMoneyboxDataState> {
        return productsRepository.makeOneOffPayment(token, amount, investorProductId)
            .map<InvestedMoneyboxDataState> { response ->
                InvestedMoneyboxDataState.Success(response)
            }.doOnError { error ->
                InvestedMoneyboxDataState.Error(error.message)
            }
    }

}