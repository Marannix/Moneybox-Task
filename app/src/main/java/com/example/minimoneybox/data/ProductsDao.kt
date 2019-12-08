package com.example.minimoneybox.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.minimoneybox.data.products.ProductResponses
import io.reactivex.Single

@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProducts(products: List<ProductResponses>)

    @Query("UPDATE products SET moneyBox = :newMoneyBox WHERE id = :id ")
    fun updateProductMoneyBox(newMoneyBox: Double, id: Int)

    @Query("select * from products")
    fun getInvestorProducts(): Single<List<ProductResponses>>

}