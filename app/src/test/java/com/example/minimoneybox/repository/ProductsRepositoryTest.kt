package com.example.minimoneybox.repository

import com.example.minimoneybox.api.ProductsApi
import org.mockito.Mockito

class ProductsRepositoryTest {

    private val api = Mockito.mock(ProductsApi::class.java)
    private lateinit var productsRepository: ProductsRepository


}