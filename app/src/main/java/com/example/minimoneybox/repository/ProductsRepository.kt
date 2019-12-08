package com.example.minimoneybox.repository

import androidx.lifecycle.MutableLiveData
import com.example.minimoneybox.api.ProductsApi
import com.example.minimoneybox.data.ProductsDao
import com.example.minimoneybox.data.products.InvestedMoneyboxResponse
import com.example.minimoneybox.data.products.ProductResponses
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val productsDao: ProductsDao,
    private val productsApi: ProductsApi
) {

    val totalValue = MutableLiveData<Double>()

    fun getInvestorProducts(token: String): Observable<List<ProductResponses>> {
        return Observable.concatArrayEagerDelayError(
            getInvestorProductsFromApi(token).toObservable(),
            getInvestorProductsFromDb()
        )
    }

    private fun getInvestorProductsFromApi(token: String) : Single<List<ProductResponses>> {
        return productsApi.getInvestorProducts(token)
            .doOnSuccess {investorProducts ->
                storeProductsInDb(investorProducts.productResponses)
                totalValue.postValue(investorProducts.totalPlanValue)
            }.map {
                it.productResponses
            }
            .subscribeOn(Schedulers.io())
    }

    fun makeOneOffPayment(token: String, moneybox: Double, amount: Int, id: Int): Observable<InvestedMoneyboxResponse> {
        return productsApi.makePayment(token, amount, id)
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                val newMoneyBoxValue = moneybox + amount.toDouble()
                updateMoneyBox(newMoneyBoxValue, id)
            }
            .toObservable()
    }

    fun totalPlanValue() {

    }

    private fun storeProductsInDb(investorProducts: List<ProductResponses>) {
        productsDao.insertProducts(investorProducts)
    }

    private fun getInvestorProductsFromDb(): Observable<List<ProductResponses>>? {
        return productsDao.getInvestorProducts()
            .toObservable()
    }

    private fun updateMoneyBox(moneyBox: Double, productId: Int) {
        return productsDao.updateProductMoneyBox(moneyBox, productId)
    }
}