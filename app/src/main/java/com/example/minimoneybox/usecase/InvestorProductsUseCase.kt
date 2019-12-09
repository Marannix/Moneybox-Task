package com.example.minimoneybox.usecase

import androidx.lifecycle.MutableLiveData
import com.example.minimoneybox.R
import com.example.minimoneybox.repository.ProductsRepository
import com.example.minimoneybox.state.InvestedMoneyboxDataState
import com.example.minimoneybox.state.InvestorProductsDataState
import io.reactivex.Observable
import retrofit2.HttpException
import javax.inject.Inject

class InvestorProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {

    fun getInvestorProductsDataState(token: String): Observable<InvestorProductsDataState> {
        return productsRepository.getInvestorProducts(token)
            .map<InvestorProductsDataState> { products ->
                InvestorProductsDataState.Success(products)
            }.doOnError { error ->
                if (error is HttpException) {
                    if (error.code() == 401) {
                        error.response().errorBody()
                        InvestorProductsDataState.Error(R.string.session_expired_error, error.code())
                    }
                } else {
                    // Not sure what error
                    InvestorProductsDataState.UnknownError(error.message, 0)
                }

            }
    }

    fun makeOneOffPayment(
        token: String,
        moneybox: Double,
        amount: Int,
        investorProductId: Int
    ): Observable<InvestedMoneyboxDataState> {
        return productsRepository.makeOneOffPayment(token, moneybox, amount, investorProductId)
            .map<InvestedMoneyboxDataState> { response ->
                InvestedMoneyboxDataState.Success(response)
            }.doOnError { error ->
                InvestedMoneyboxDataState.Error(error.message)
            }
    }

    fun getTotalPlanValue(): MutableLiveData<Double> {
        return productsRepository.totalValue
    }

}