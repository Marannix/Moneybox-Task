package com.example.minimoneybox.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minimoneybox.api.ProductsApi
import com.example.minimoneybox.data.ProductsDao
import com.example.minimoneybox.data.products.InvestedMoneyboxResponse
import com.example.minimoneybox.data.products.InvestorProductsResponse
import com.example.minimoneybox.data.products.ProductResponses
import com.example.minimoneybox.data.products.Products
import com.example.minimoneybox.data.user.UserResponse
import com.example.minimoneybox.utils.PriceUtils
import com.example.minimoneybox.utils.RxImmediateSchedulerRule
import com.example.minimoneybox.utils.TEN_POUND_PRICE
import com.example.minimoneybox.utils.UnitTestUtils
import com.google.gson.GsonBuilder
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class ProductsRepositoryTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val api = Mockito.mock(ProductsApi::class.java)
    private val dao = Mockito.mock(ProductsDao::class.java)

    private lateinit var productResponse: InvestorProductsResponse
    private lateinit var userResponse: UserResponse
    private lateinit var moneyBoxResponse: InvestedMoneyboxResponse
    private lateinit var repository: ProductsRepository

    private lateinit var isa: ProductResponses

    private var result = emptyList<ProductResponses>()

    @Before
    fun setUp() {
        retrieveJson()
        createIsa()
        repository = ProductsRepository(dao, api)
    }

    @Test
    fun `when calling the network, parse correctly the product response`() {
        `when`(api.getInvestorProducts(userResponse.session.bearerToken))
            .thenReturn(Single.just(productResponse))
        `when`(dao.getInvestorProducts()).thenReturn(Single.just(productResponse.productResponses))

        repository.getInvestorProducts(userResponse.session.bearerToken)
            .blockingSubscribe {
                result = it
            }

        assertThat(result.size).isEqualTo(productResponse.productResponses.size)
        assertThat(result.first()).isEqualTo(productResponse.productResponses.first())
        assertThat(result.last()).isEqualTo(productResponse.productResponses.last())

        verify(api).getInvestorProducts(userResponse.session.bearerToken)
    }

    @Test
    fun `when network fails, parse correctly the response from database`() {
        `when`(api.getInvestorProducts(userResponse.session.bearerToken)).thenReturn(Single.error(Throwable("")))
        `when`(dao.getInvestorProducts()).thenReturn(Single.just(productResponse.productResponses))

        repository.getInvestorProducts(userResponse.session.bearerToken)
            .blockingSubscribe { response ->
                result = response
            }

        assertThat(result.size).isEqualTo(productResponse.productResponses.size)
        assertThat(result.first()).isEqualTo(productResponse.productResponses.first())
        assertThat(result.last()).isEqualTo(productResponse.productResponses.last())

        verify(api).getInvestorProducts(userResponse.session.bearerToken)

        verify(dao).getInvestorProducts()
    }

    @Test
    fun `given network succeed and product id is isa and successful one off payment success then increment moneybox`() {
        `when`(
            api.makePayment(
                userResponse.session.bearerToken,
                TEN_POUND_PRICE,
                isa.id
            )
        )
            .thenReturn(Single.just(moneyBoxResponse))

        repository.makeOneOffPayment(
            userResponse.session.bearerToken,
            isa.moneyBox,
            TEN_POUND_PRICE,
            isa.id
        ).blockingSubscribe { response ->
            moneyBoxResponse = response
        }
        assertThat(moneyBoxResponse.moneyBox).isEqualTo(PriceUtils.formatDecimal().format(isa.moneyBox + TEN_POUND_PRICE))
    }

    private fun retrieveJson() {
        val response = UnitTestUtils.readJsonFile("investorproducts.json")
        val userFileResponse = UnitTestUtils.readJsonFile("user.json")
        val moneyBoxFileResponse = UnitTestUtils.readJsonFile("moneybox.json")
        productResponse = GsonBuilder().create().fromJson(response, InvestorProductsResponse::class.java)
        userResponse = GsonBuilder().create().fromJson(userFileResponse, UserResponse::class.java)
        moneyBoxResponse = GsonBuilder().create().fromJson(moneyBoxFileResponse, InvestedMoneyboxResponse::class.java)
    }

    private fun createIsa() {
        isa = ProductResponses(
            productResponse.productResponses[0].id,
            productResponse.productResponses[0].planValue,
            productResponse.productResponses[0].moneyBox,
            Products(
                productResponse.productResponses[0].products.id,
                productResponse.productResponses[0].products.friendlyName
            )
        )
    }

}