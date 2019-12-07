package com.example.minimoneybox.repository

import com.example.minimoneybox.api.ProductsApi
import com.example.minimoneybox.data.products.InvestedMoneyboxResponse
import com.example.minimoneybox.data.products.InvestorProducts
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val productsApi: ProductsApi
) {

    fun getInvestorProducts(token: String): Observable<InvestorProducts> {
        return getInvestorProductsFromApi(token).toObservable()
    }

    // TODO: I wouldn't really need to save this information when the user is offline
    // Would think normally bank would ask the user to signin again with their pin
    // NOTE: Maybe I do as when the figure updates I want to update and retrieve from DB?
    private fun getInvestorProductsFromApi(token: String): Single<InvestorProducts> {
        return productsApi.getInvestorProducts(token)
            .subscribeOn(Schedulers.io())
    }

    fun makeOneOffPayment(token: String, amount: Int, id: Int): Observable<InvestedMoneyboxResponse> {
        return productsApi.makePayment(token, amount, id)
            .subscribeOn(Schedulers.io())
            .toObservable()
    }

}