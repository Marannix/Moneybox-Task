package com.example.minimoneybox.activity

import android.os.Bundle
import android.util.Log
import com.example.minimoneybox.R
import com.example.minimoneybox.User

class DashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val extras = intent.extras
        if (extras != null) {
            val user : User = extras.getParcelable("user")
            Log.d("Dashboard", user.firstName)
        }
    }
}
