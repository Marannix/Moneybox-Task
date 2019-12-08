package com.example.minimoneybox.dagger.modules

import androidx.fragment.app.FragmentActivity
import com.example.minimoneybox.activity.LoginActivity
import com.example.minimoneybox.fragment.LoginFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LoginActivityModule {

    @Binds
    abstract fun provideLoginActivity(activity: LoginActivity): FragmentActivity

    @ContributesAndroidInjector
    abstract fun loginFragment(): LoginFragment

}