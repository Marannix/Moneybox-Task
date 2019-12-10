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

    // I needed a way to retrieve the total value and this one of the ways which came to mind
    val totalValue = MutableLiveData<Double>()

    /**
     *  A comparision is made between the list retrieved from the api and the list stored in the database.
     *  If the api returns a empty list or fails and the database contains a list then the array given will be of the database
     */
    fun getInvestorProducts(token: String): Observable<List<ProductResponses>> {
        return Observable.concatArrayEagerDelayError(
            getInvestorProductsFromApi(token).toObservable(),
            getInvestorProductsFromDb()
        )
    }

    /**
     *  Get the list of the three type of investment plan and store them inside the database
     */
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

    /**
     *  Given the user bearer token, product id (i.e isa), amount being increment to the moneybox and the original moneybox value,
     *  I send a request to the backend to update the moneybox value and store the new value inside the database
     */
    fun makeOneOffPayment(token: String, moneybox: Double, amount: Int, id: Int): Observable<InvestedMoneyboxResponse> {
        return productsApi.makePayment(token, amount, id)
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                val newMoneyBoxValue = moneybox + amount.toDouble()
                updateMoneyBox(newMoneyBoxValue, id)
            }
            .toObservable()
    }

    /**
     *  Stores the investment product inside the database
     */
    private fun storeProductsInDb(investorProducts: List<ProductResponses>) {
        productsDao.insertProducts(investorProducts)
    }

    /**
     *  Retrieves the investment product from the database
     */
    private fun getInvestorProductsFromDb(): Observable<List<ProductResponses>>? {
        return productsDao.getInvestorProducts()
            .toObservable()
            .flatMap { list ->
                return@flatMap if (list.isEmpty()) {
                    Observable.empty()
                } else {
                    Observable.just(list)
                }
            }
    }

    /**
     *  After the one of payment is successful, the money box inside the database is updated with a new value
     */
    private fun updateMoneyBox(moneyBox: Double, productId: Int) {
        return productsDao.updateProductMoneyBox(moneyBox, productId)
    }
}