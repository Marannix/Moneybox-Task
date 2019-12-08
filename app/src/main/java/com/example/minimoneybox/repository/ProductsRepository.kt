package com.example.minimoneybox.repository

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


//    fun getCharacters(): Observable<List<CharactersResults>> {
//        // Added concatArrayEagerDelayError to repository when fetching data from api and database,
//        //  it delays any errors (no network from api) until both api and database source terminate
//        return Observable.concatArrayEagerDelayError(
//            getCharactersFromApi(1).toObservable(),
//            getCharactersFromDb()
//        )
//    }

    fun getInvestorProducts(token: String): Observable<List<ProductResponses>> {
        return Observable.concatArrayEagerDelayError(
            getInvestorProductsFromApi(token).toObservable(),
            getInvestorProductsFromDb()
        )
    }

    //


//    fun getInvestorProducts(token: String): Observable<InvestorProducts> {
//        return getInvestorProductsFromApi(token).toObservable()
//    }

    // TODO: I wouldn't really need to save this information when the user is offline
    // Would think normally bank would ask the user to signin again with their pin
    // NOTE: Maybe I do as when the figure updates I want to update and retrieve from DB?
    private fun getInvestorProductsFromApi(token: String) : Single<List<ProductResponses>> {
        return productsApi.getInvestorProducts(token)
            .doOnSuccess {investorProducts ->
                storeProductsInDb(investorProducts.productResponses)
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