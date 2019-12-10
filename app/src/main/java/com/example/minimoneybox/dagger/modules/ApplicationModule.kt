package com.example.minimoneybox.dagger.modules

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    internal fun provideContext(application: Application): Context {
        return application.baseContext
    }

    @Provides
    internal fun provideConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}