package com.example.minimoneybox.repository

import com.example.minimoneybox.api.ProductsApi
import com.example.minimoneybox.data.ProductsDao
import com.example.minimoneybox.data.products.InvestorProducts
import com.example.minimoneybox.data.products.ProductResponses
import com.example.minimoneybox.data.user.UserResponse
import com.example.minimoneybox.utils.UnitTestUtils
import com.google.gson.GsonBuilder
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class ProductsRepositoryTest {

    private val api = Mockito.mock(ProductsApi::class.java)
    private val dao = Mockito.mock(ProductsDao::class.java)

    private lateinit var productResponse: InvestorProducts
    private lateinit var userResponse: UserResponse
    private lateinit var repository: ProductsRepository

    private var result = emptyList<ProductResponses>()

    @Before
    fun setUp() {
        val response = UnitTestUtils.readJsonFile("investorproducts.json")
        val userFileResponse = UnitTestUtils.readJsonFile("user.json")
        productResponse = GsonBuilder().create().fromJson(response, InvestorProducts::class.java)
        userResponse = GsonBuilder().create().fromJson(userFileResponse, UserResponse::class.java)
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
            .blockingSubscribe {
                result = it
            }

        assertThat(result.size).isEqualTo(productResponse.productResponses.size)
        assertThat(result.first()).isEqualTo(productResponse.productResponses.first())
        assertThat(result.last()).isEqualTo(productResponse.productResponses.last())

        verify(api).getInvestorProducts(userResponse.session.bearerToken)

        verify(dao).getInvestorProducts()
    }
}