package com.example.minimoneybox.repository

import com.example.minimoneybox.data.products.InvestorProducts
import com.example.minimoneybox.api.ProductsApi
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
    private fun getInvestorProductsFromApi(token: String): Single<InvestorProducts> {
        return productsApi.getInvestorProducts(token)
            .subscribeOn(Schedulers.io())
    }

}