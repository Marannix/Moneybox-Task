package com.example.minimoneybox.activity

import android.content.Intent
import android.os.Bundle
import com.example.minimoneybox.R
import com.example.minimoneybox.fragment.LoginFragment

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // When there is no internet "log the user out"
        when (userPreference.hasUserLoggedIn() && networkUtil.isNetworkAvailable()) {
            true -> launchDashboardActivity()
            false ->  launchLoginFragment()
        }
    }

    private fun launchLoginFragment() {
        val fragment = LoginFragment.newInstance()
        fragment.attach(object : LoginFragment.OnLoginSuccessListener {
            override fun onLoginSuccess() {
                launchDashboardActivity()
            }
        })
        addFragment(fragment)
    }

    private fun launchDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}
