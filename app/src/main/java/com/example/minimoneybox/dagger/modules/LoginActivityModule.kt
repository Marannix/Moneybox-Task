package com.example.minimoneybox.dagger.modules

import androidx.fragment.app.FragmentActivity
import com.example.minimoneybox.activity.LoginActivity
import dagger.Binds
import dagger.Module

@Module
abstract class LoginActivityModule {
    @Binds
    abstract fun provideLoginActivity(activity: LoginActivity): FragmentActivity
}