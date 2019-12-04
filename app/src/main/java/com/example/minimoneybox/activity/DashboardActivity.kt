package com.example.minimoneybox.activity

import android.os.Bundle
import android.util.Log
import com.example.minimoneybox.R
import com.example.minimoneybox.User
import com.google.android.material.card.MaterialCardView

class DashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val extras = intent.extras
        if (extras != null) {
            val user : User = extras.getParcelable("user")
            Log.d("Dashboard", user.firstName)
        }

        val stockAndShareLayout = findViewById<MaterialCardView>(R.id.stockAndShareLayout)
        val planValueLabel = findViewById<MaterialCardView>(R.id.generalInvestmentAccountLayout)
        val moneyBoxLabel = findViewById<MaterialCardView>(R.id.lifetimeISALayout)

    }
}
