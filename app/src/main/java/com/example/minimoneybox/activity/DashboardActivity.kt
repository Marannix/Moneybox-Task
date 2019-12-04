package com.example.minimoneybox.activity

import android.os.Bundle
import com.example.minimoneybox.R
import com.google.android.material.card.MaterialCardView

class DashboardActivity : BaseActivity() {

    //TODO: Create fragment and retrieve user information from the database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val stockAndShareLayout = findViewById<MaterialCardView>(R.id.stockAndShareLayout)
        val planValueLabel = findViewById<MaterialCardView>(R.id.generalInvestmentAccountLayout)
        val moneyBoxLabel = findViewById<MaterialCardView>(R.id.lifetimeISALayout)
    }
}
