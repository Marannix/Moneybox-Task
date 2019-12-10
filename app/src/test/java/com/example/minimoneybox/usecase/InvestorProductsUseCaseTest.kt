package com.example.minimoneybox.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minimoneybox.api.ProductsApi
import com.example.minimoneybox.data.ProductsDao
import com.example.minimoneybox.data.products.InvestedMoneyboxResponse
import com.example.minimoneybox.data.products.InvestorProductsResponse
import com.example.minimoneybox.data.products.ProductResponses
import com.example.minimoneybox.data.products.Products
import com.example.minimoneybox.data.user.UserResponse
import com.example.minimoneybox.repository.ProductsRepository
import com.example.minimoneybox.state.InvestedMoneyboxDataState
import com.example.minimoneybox.state.InvestorProductsDataState
import com.example.minimoneybox.utils.RxImmediateSchedulerRule
import com.example.minimoneybox.utils.TEN_POUND_PRICE
import com.example.minimoneybox.utils.UnitTestUtils
import com.google.gson.GsonBuilder
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class InvestorProductsUseCaseTest {
    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val api = Mockito.mock(ProductsApi::class.java)
    private val dao = Mockito.mock(ProductsDao::class.java)

    private val productsRepository by lazy { ProductsRepository(dao, api) }
    private val productUseCase by lazy { InvestorProductsUseCase(productsRepository) }

    private lateinit var productResponse: InvestorProductsResponse
    private lateinit var moneyBoxResponse: InvestedMoneyboxResponse
    private lateinit var userResponse: UserResponse

    private lateinit var investorProductsDatastate: InvestorProductsDataState
    private lateinit var expectedInvestorProductsSuccessState: InvestorProductsDataState.Success
    private lateinit var moneyboxDataState: InvestedMoneyboxDataState
    private lateinit var expectedMoneyboxSuccessState: InvestedMoneyboxDataState.Success

    private lateinit var isa: ProductResponses
    private lateinit var lisa: ProductResponses
    private lateinit var gia: ProductResponses

    @Before
    fun setUp() {
        retrieveJson()
        setExpectedStates()
        createProducts()
    }

    @Test
    fun `when network succeed, emit investor product data success state`() {
        Mockito.`when`(api.getInvestorProducts(userResponse.session.bearerToken))
            .thenReturn(Single.just(productResponse))
        Mockito.`when`(dao.getInvestorProducts()).thenReturn(Single.just(productResponse.productResponses))

        productUseCase.getInvestorProductsDataState(userResponse.session.bearerToken).subscribe { dataState ->
            investorProductsDatastate = dataState
        }

        Assert.assertEquals(investorProductsDatastate, expectedInvestorProductsSuccessState)
    }

    @Test
    fun `given network succeed and product id is isa, emit successful one off payment success state,`() {
        Mockito.`when`(
            api.makePayment(
                userResponse.session.bearerToken,
                TEN_POUND_PRICE,
                isa.id
            )
        )
            .thenReturn(Single.just(moneyBoxResponse))

        productUseCase.makeOneOffPayment(
            userResponse.session.bearerToken,
            isa.moneyBox,
            TEN_POUND_PRICE,
            isa.id
        ).subscribe { dataState ->
            moneyboxDataState = dataState
        }

        Assert.assertEquals(moneyboxDataState, expectedMoneyboxSuccessState)
    }

    @Test
    fun `given network succeed and product id is lisa, emit successful one off payment success state,`() {
        Mockito.`when`(
            api.makePayment(
                userResponse.session.bearerToken,
                TEN_POUND_PRICE,
                lisa.id
            )
        )
            .thenReturn(Single.just(moneyBoxResponse))

        productUseCase.makeOneOffPayment(
            userResponse.session.bearerToken,
            lisa.moneyBox,
            TEN_POUND_PRICE,
            lisa.id
        ).subscribe { dataState ->
            moneyboxDataState = dataState
        }

        Assert.assertEquals(moneyboxDataState, expectedMoneyboxSuccessState)
    }

    @Test
    fun `given network succeed and product id is gia, emit successful one off payment success state,`() {
        Mockito.`when`(
            api.makePayment(
                userResponse.session.bearerToken,
                TEN_POUND_PRICE,
                gia.id
            )
        )
            .thenReturn(Single.just(moneyBoxResponse))

        productUseCase.makeOneOffPayment(
            userResponse.session.bearerToken,
            gia.moneyBox,
            TEN_POUND_PRICE,
            gia.id
        ).subscribe { dataState ->
            moneyboxDataState = dataState
        }

        Assert.assertEquals(moneyboxDataState, expectedMoneyboxSuccessState)
    }

    private fun retrieveJson() {
        val response = UnitTestUtils.readJsonFile("investorproducts.json")
        val userFileResponse = UnitTestUtils.readJsonFile("user.json")
        val moneyBoxFileResponse = UnitTestUtils.readJsonFile("moneybox.json")
        productResponse = GsonBuilder().create().fromJson(response, InvestorProductsResponse::class.java)
        userResponse = GsonBuilder().create().fromJson(userFileResponse, UserResponse::class.java)
        moneyBoxResponse = GsonBuilder().create().fromJson(moneyBoxFileResponse, InvestedMoneyboxResponse::class.java)
    }

    private fun setExpectedStates() {
        expectedInvestorProductsSuccessState = InvestorProductsDataState.Success(productResponse.productResponses)
        expectedMoneyboxSuccessState = InvestedMoneyboxDataState.Success(moneyBoxResponse)
    }

    private fun createProducts() {
        createIsa()
        createLisa()
        createGia()
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

    private fun createLisa() {
        lisa = ProductResponses(
            productResponse.productResponses[1].id,
            productResponse.productResponses[1].planValue,
            productResponse.productResponses[1].moneyBox,
            Products(
                productResponse.productResponses[1].products.id,
                productResponse.productResponses[1].products.friendlyName
            )
        )
    }

    private fun createGia() {
        gia = ProductResponses(
            productResponse.productResponses[2].id,
            productResponse.productResponses[2].planValue,
            productResponse.productResponses[2].moneyBox,
            Products(
                productResponse.productResponses[2].products.id,
                productResponse.productResponses[2].products.friendlyName
            )
        )
    }
}