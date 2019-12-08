package com.example.minimoneybox.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.minimoneybox.R
import com.example.minimoneybox.fragment.LoginFragment

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        when (userPreference.hasUserLoggedIn()) {
            true -> launchDashboardActivity()
            false ->  launchLoginFragment()
        }
        launchLoginFragment()
    }

    private fun launchLoginFragment() {
        val fragment = LoginFragment.newInstance()
        fragment.attach(object : LoginFragment.OnLoginSuccessListener {
            override fun onLoginSuccess() {
                launchDashboardActivity()
            }
        })
        replaceFragment(fragment)
    }

    private fun launchDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    //Fragment is created twice at launch? Replace fragment instead of adding to avoid duplicated fragment shown
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentPopped = fragmentManager.popBackStackImmediate(fragment.javaClass.name, 0)

        if (!fragmentPopped) {
            fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(fragment.javaClass.name)
                .commit()
        }
    }

}
