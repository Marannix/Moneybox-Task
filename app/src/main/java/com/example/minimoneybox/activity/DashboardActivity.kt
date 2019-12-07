package com.example.minimoneybox.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.minimoneybox.R.id.fragmentContainer
import com.example.minimoneybox.R.layout.activity_dashboard
import com.example.minimoneybox.data.products.ProductResponses
import com.example.minimoneybox.fragment.DashboardFragment
import com.example.minimoneybox.fragment.InvestmentFragment


class DashboardActivity : BaseActivity() {

    private var fragmentName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_dashboard)

        if (savedInstanceState == null) {
            initDashboardFragment()
        } else {
            supportFragmentManager.findFragmentByTag(fragmentName)
        }
    }

    private fun initDashboardFragment() {
        val fragment = DashboardFragment.newInstance()
        fragment.attach(object : DashboardFragment.OnProductsSelectedListener {
            override fun onIsaSelected(isa: ProductResponses) {
                initInvestmentFragment(isa)
            }

            override fun onGiaSelected(gia: ProductResponses) {
                initInvestmentFragment(gia)
            }

            override fun onLisaSelected(lisa: ProductResponses) {
                initInvestmentFragment(lisa)
            }
        })
        replaceFragment(fragment)
    }

    private fun initInvestmentFragment(isa: ProductResponses) {
        val fragment = InvestmentFragment.newInstance(isa)
        replaceFragment(fragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        fragmentName = fragment.javaClass.name
        val fragmentManager = supportFragmentManager
        val fragmentPopped = fragmentManager.popBackStackImmediate(fragmentName, 0)

        if (!fragmentPopped) {
            fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .add(fragmentContainer, fragment)
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
