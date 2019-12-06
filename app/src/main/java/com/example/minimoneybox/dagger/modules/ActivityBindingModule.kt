package com.example.minimoneybox.dagger.modules

import com.example.minimoneybox.activity.DashboardActivity
import com.example.minimoneybox.activity.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [LoginActivityModule::class])
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [DashboardActivityModule::class])
    abstract fun dashboardActivity(): DashboardActivity
}