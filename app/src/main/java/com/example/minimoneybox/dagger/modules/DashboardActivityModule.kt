package com.example.minimoneybox.dagger.modules

import androidx.fragment.app.FragmentActivity
import com.example.minimoneybox.activity.DashboardActivity
import com.example.minimoneybox.fragment.DashboardFragment
import com.example.minimoneybox.fragment.InvestmentFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DashboardActivityModule {
    @Binds
    abstract fun provideDashboardActivity(activity: DashboardActivity): FragmentActivity

    @ContributesAndroidInjector
    abstract fun dashboardFragment(): DashboardFragment

    @ContributesAndroidInjector
    abstract fun investmentFragment(): InvestmentFragment
}