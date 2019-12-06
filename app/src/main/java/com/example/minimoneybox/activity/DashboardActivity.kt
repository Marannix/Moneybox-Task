package com.example.minimoneybox.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.minimoneybox.fragment.DashboardFragment
import com.example.minimoneybox.fragment.InvestmentFragment


class DashboardActivity : BaseActivity() {

    private var fragmentName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.minimoneybox.R.layout.activity_dashboard)

        if (savedInstanceState == null) {
            initDashboardFragment()
        } else {
            supportFragmentManager.findFragmentByTag(fragmentName)
        }
    }

    private fun initDashboardFragment() {
        val fragment = DashboardFragment.newInstance()
        fragment.attach(object : DashboardFragment.OnProductsSelectedListener {
            override fun onISASelected() {
                initInvestmentFragment()
            }
        })
        replaceFragment(fragment)
    }

    private fun initInvestmentFragment() {
        val fragment = InvestmentFragment.newInstance()
        replaceFragment(fragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        fragmentName = fragment.javaClass.name
        val fragmentManager = supportFragmentManager
        val fragmentPopped = fragmentManager.popBackStackImmediate(fragmentName, 0)

        if (!fragmentPopped) {
            fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(com.example.minimoneybox.R.id.fragmentContainer, fragment)
                .addToBackStack(fragmentName)
                .commit()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

}
