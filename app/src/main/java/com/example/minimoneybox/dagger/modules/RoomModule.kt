package com.example.minimoneybox.dagger.modules

import android.app.Application
import androidx.room.Room
import com.example.minimoneybox.data.ProductsDao
import com.example.minimoneybox.data.UsersDao
import com.example.minimoneybox.database.ApplicationDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(application: Application): ApplicationDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            ApplicationDatabase::class.java, "moneybox.db"
        )
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(applicationDatabase: ApplicationDatabase): UsersDao {
        return applicationDatabase.usersDao()
    }

    @Singleton
    @Provides
    fun provideInvestorProductsDao(applicationDatabase: ApplicationDatabase): ProductsDao {
        return applicationDatabase.productsDao()
    }
}