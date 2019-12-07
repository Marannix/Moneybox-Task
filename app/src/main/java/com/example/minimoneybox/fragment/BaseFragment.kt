package com.example.minimoneybox.fragment

import androidx.lifecycle.ViewModelProvider
import com.example.minimoneybox.sharedpreferences.PreferencesHelper
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var userPreference: PreferencesHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

}