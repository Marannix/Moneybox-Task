package com.example.minimoneybox.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minimoneybox.R
import com.example.minimoneybox.state.InvestedMoneyboxDataState
import com.example.minimoneybox.state.InvestedMoneyboxViewState
import com.example.minimoneybox.state.InvestorProductsDataState
import com.example.minimoneybox.state.InvestorProductsViewState
import com.example.minimoneybox.usecase.InvestorProductsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import javax.inject.Inject

class InvestorProductsViewModel @Inject constructor(
    private val investorProductsUseCase: InvestorProductsUseCase
) : ViewModel() {

    private val disposables = CompositeDisposable()
    val viewState = MutableLiveData<InvestorProductsViewState>()
    val paymentViewState = MutableLiveData<InvestedMoneyboxViewState>()

    fun getInvestorProductsInformation(token: String) {
        disposables.add(
            investorProductsUseCase.getInvestorProductsDataState(token)
                .observeOn(AndroidSchedulers.mainThread())
                .map { investorProductsDataState ->
                    return@map when (investorProductsDataState) {
                        is InvestorProductsDataState.Success -> {
                            // TODO Find a better way to retrieve the products, maybe check if the name matches?
                            InvestorProductsViewState.ShowProducts(
                                investorProductsDataState.investorProducts[0],
                                investorProductsDataState.investorProducts[1],
                                investorProductsDataState.investorProducts[2],
                                //TODO: Find a better way to get this value
                                investorProductsUseCase.getTotalPlanValue().value!!
                                // Maybe I should just pass the entire investorProducts cause this is weird
                            )
                        }
                        is InvestorProductsDataState.Error -> {
                            InvestorProductsViewState.ShowError(investorProductsDataState.errorMessage, investorProductsDataState.errorCode)
                        }
                        is InvestorProductsDataState.UnknownError -> {
                            InvestorProductsViewState.ShowUnknownError(investorProductsDataState.errorMessage, investorProductsDataState.errorCode)
                        }
                    }
                }.doOnSubscribe { viewState.value = InvestorProductsViewState.Loading }
                .subscribe({
                    this.viewState.value = it
                }, {error ->
                    //Added this to prevent crashing when device goes offline
                    if (error is HttpException) {
                        if (error.code() == 401) {
                            this.viewState.value = InvestorProductsViewState.ShowError(R.string.session_expired_error, error.code())
                        }
                    } else {
                        // Not sure what error
                        this.viewState.value =  InvestorProductsViewState.ShowUnknownError(error.message, 0)
                    }

//                    this.viewState.value = InvestorProductsViewState.ShowUnknownError(it.message, 0)
                })
        )
    }

    fun makePayment(token: String, moneybox: Double, amount: Int, investorProductId: Int) {
        disposables.add(
            investorProductsUseCase.makeOneOffPayment(token, moneybox, amount, investorProductId).observeOn(AndroidSchedulers.mainThread())
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

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}