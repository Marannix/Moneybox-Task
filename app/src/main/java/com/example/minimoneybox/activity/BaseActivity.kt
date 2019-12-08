package com.example.minimoneybox.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import com.example.minimoneybox.R
import com.example.minimoneybox.sharedpreferences.PreferencesHelper
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    private var fragmentName = ""

    @Inject
    lateinit var userPreference: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState, persistentState)
    }

    fun replaceFragment(fragment: Fragment) {
        fragmentName = fragment.javaClass.name
        val fragmentManager = supportFragmentManager
        val fragmentPopped = fragmentManager.popBackStackImmediate(fragmentName, 0)

        if (!fragmentPopped) {
            fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .add(R.id.fragmentContainer, fragment)
                .addToBackStack(fragmentName)
                .commit()
        }
    }

}