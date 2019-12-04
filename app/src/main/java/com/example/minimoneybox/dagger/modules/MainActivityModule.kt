package com.example.minimoneybox.dagger.modules

import androidx.fragment.app.FragmentActivity
import com.example.minimoneybox.activity.LoginActivity
import dagger.Binds
import dagger.Module

@Module
abstract class MainActivityModule {

    @Binds
    abstract fun provideMainActivity(activity: LoginActivity): FragmentActivity
}