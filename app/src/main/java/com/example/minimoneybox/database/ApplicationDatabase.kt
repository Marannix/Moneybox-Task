package com.example.minimoneybox.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.minimoneybox.data.Converters
import com.example.minimoneybox.data.ProductsDao
import com.example.minimoneybox.data.UsersDao
import com.example.minimoneybox.data.products.ProductResponses
import com.example.minimoneybox.data.user.User

@Database(
    entities = [User::class, ProductResponses::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun productsDao(): ProductsDao
}