package com.example.minimoneybox.dagger.modules

import com.example.minimoneybox.activity.DashboardActivity
import com.example.minimoneybox.activity.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeDashboardActivity(): DashboardActivity
}