package com.example.minimoneybox.activity

import android.os.Bundle
import android.os.PersistableBundle
import com.example.minimoneybox.sharedpreferences.PreferencesHelper
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


abstract class BaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var userPreference: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState, persistentState)
    }
}