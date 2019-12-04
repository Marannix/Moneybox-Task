package com.example.minimoneybox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * Unable to @Inject my ViewModel, needed to create a custom ViewModelFactory
 * https://proandroiddev.com/viewmodel-with-dagger2-architecture-components-2e06f06c9455
 */

class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}