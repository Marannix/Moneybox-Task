package com.example.minimoneybox.dagger.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minimoneybox.viewmodel.InvestorProductsViewModel
import com.example.minimoneybox.viewmodel.UsersViewModel
import com.example.minimoneybox.viewmodel.ViewModelFactory
import com.example.minimoneybox.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(UsersViewModel::class)
    internal abstract fun bindingUserViewModel(viewModel: UsersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InvestorProductsViewModel::class)
    internal abstract fun bindingInvestorProductViewModel(viewModel: InvestorProductsViewModel): ViewModel

}