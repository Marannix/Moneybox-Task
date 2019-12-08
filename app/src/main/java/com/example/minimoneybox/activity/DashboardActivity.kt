package com.example.minimoneybox.activity

import android.os.Bundle
import com.example.minimoneybox.R.layout.activity_dashboard
import com.example.minimoneybox.data.products.ProductResponses
import com.example.minimoneybox.fragment.DashboardFragment
import com.example.minimoneybox.fragment.InvestmentFragment


class DashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_dashboard)
        initDashboardFragment()
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
        addFragment(fragment)
    }

    private fun initInvestmentFragment(isa: ProductResponses) {
        val fragment = InvestmentFragment.newInstance(isa)
        addFragment(fragment)
    }

}
