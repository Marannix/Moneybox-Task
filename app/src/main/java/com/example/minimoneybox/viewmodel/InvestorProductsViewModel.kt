package com.example.minimoneybox.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minimoneybox.repository.ProductsRepository
import com.example.minimoneybox.state.InvestedMoneyboxDataState
import com.example.minimoneybox.state.InvestedMoneyboxViewState
import com.example.minimoneybox.state.InvestorProductsDataState
import com.example.minimoneybox.state.InvestorProductsViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class InvestorProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()
    val viewState = MutableLiveData<InvestorProductsViewState>()
    val paymentViewState = MutableLiveData<InvestedMoneyboxViewState>()

    fun getInvestorProductsInformation(token: String) {
        disposables.add(
            getInvestoryProductsDataState(token).observeOn(AndroidSchedulers.mainThread())
                .map { investorProductsDataState ->
                    return@map when (investorProductsDataState) {
                        is InvestorProductsDataState.Success -> {
                            // TODO Find a better way to retrieve the products, maybe check if the name matches?
                            InvestorProductsViewState.ShowProducts(
                                investorProductsDataState.investorProducts.productResponses[0],
                                investorProductsDataState.investorProducts.productResponses[1],
                                investorProductsDataState.investorProducts.productResponses[2],
                                // Maybe I should just pass the entire investorProducts cause this is weird
                                investorProductsDataState.investorProducts.totalPlanValue
                            )
                        }
                        is InvestorProductsDataState.Error -> {
                            InvestorProductsViewState.ShowError(investorProductsDataState.errorMessage)
                        }
                    }
                }.doOnSubscribe { viewState.value = InvestorProductsViewState.Loading }
                .subscribe({
                    this.viewState.value = it
                }, {
                    //Added this to prevent crashing when device goes offline
                    this.viewState.value = InvestorProductsViewState.ShowError(it.message)
                })
        )
    }

    fun makePayment(token: String, amount: Int, investorProductId: Int) {
        disposables.add(
            makeOneOffPayment(token, amount, investorProductId).observeOn(AndroidSchedulers.mainThread())
                .map { datastate ->
                    return@map when (datastate) {
                        is InvestedMoneyboxDataState.Success -> {
                            InvestedMoneyboxViewState.ShowUpdatedMoneyBox(datastate.response)
                        }
                        is InvestedMoneyboxDataState.Error -> {
                            InvestedMoneyboxViewState.ShowError(datastate.errorMessage)
                        }
                    }
                }.doOnSubscribe { paymentViewState.value = InvestedMoneyboxViewState.Loading }
                .subscribe({
                    this.paymentViewState.value = it
                }, {
                    //Added this to prevent crashing when device goes offline
                    this.paymentViewState.value = InvestedMoneyboxViewState.ShowError(it.message)
                })

        )
    }

    //TODO: Fix typo
    // Move to Usecase
    private fun getInvestoryProductsDataState(token: String): Observable<InvestorProductsDataState> {
        return productsRepository.getInvestorProducts(token)
            .map<InvestorProductsDataState> { products ->
                InvestorProductsDataState.Success(products)
            }.doOnError { error ->
                InvestorProductsDataState.Error(error.message)
            }
    }

    // Move to Usecase
    private fun makeOneOffPayment(
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

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}