package com.example.minimoneybox.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minimoneybox.api.ProductsApi
import com.example.minimoneybox.data.ProductsDao
import com.example.minimoneybox.data.products.InvestedMoneyboxResponse
import com.example.minimoneybox.data.products.InvestorProducts
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

private const val a_error_message = "No Network"

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

    private lateinit var productResponse: InvestorProducts
    private lateinit var moneyBoxResponse: InvestedMoneyboxResponse
    private lateinit var userResponse: UserResponse

    private lateinit var investorProductsDatastate: InvestorProductsDataState
    private lateinit var expectedInvestorProductsSuccessState: InvestorProductsDataState.Success

    private lateinit var moneyboxDataState: InvestedMoneyboxDataState
    private lateinit var expectedMoneyboxSuccessState: InvestedMoneyboxDataState.Success

    @Before
    fun setUp() {
        val response = UnitTestUtils.readJsonFile("investorproducts.json")
        val userFileResponse = UnitTestUtils.readJsonFile("user.json")
        val moneyBoxFileResponse = UnitTestUtils.readJsonFile("moneybox.json")
        productResponse = GsonBuilder().create().fromJson(response, InvestorProducts::class.java)
        userResponse = GsonBuilder().create().fromJson(userFileResponse, UserResponse::class.java)
        moneyBoxResponse = GsonBuilder().create().fromJson(moneyBoxFileResponse, InvestedMoneyboxResponse::class.java)
        investorProductsDatastate = InvestorProductsDataState.Success(productResponse.productResponses)
        expectedMoneyboxSuccessState = InvestedMoneyboxDataState.Success(moneyBoxResponse)
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
    fun `when network succeed, emit successful one off payment`() {
        Mockito.`when`(
            api.makePayment(
                userResponse.session.bearerToken,
                TEN_POUND_PRICE,
                productResponse.productResponses.get(0).products.id
            )
        )
            .thenReturn(Single.just(moneyBoxResponse))

        productUseCase.makeOneOffPayment(
            userResponse.session.bearerToken,
            productResponse.productResponses.get(0).moneyBox,
            TEN_POUND_PRICE,
            productResponse.productResponses.get(0).products.id
        ).subscribe { dataState ->
            moneyboxDataState = dataState
        }

        Assert.assertEquals(moneyboxDataState, expectedMoneyboxSuccessState)
    }
}