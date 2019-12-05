package com.example.minimoneybox.activity

import android.os.Bundle
import android.widget.Toast
import com.example.minimoneybox.R
import com.example.minimoneybox.api.ProductsApi
import com.example.minimoneybox.repository.UsersRepository
import com.google.android.material.card.MaterialCardView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DashboardActivity : BaseActivity() {

    //TODO: Create fragment and retrieve user information from the database

    @Inject
    lateinit var usersRepository: UsersRepository
    @Inject
    lateinit var productsApi: ProductsApi

    val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        if (intent.hasExtra("bearerToken")) {
            val token = intent.getStringExtra("bearerToken")
            val disposable = productsApi.getInvestorProducts("Bearer $token")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(this, it.productResponses[0].id.toString(), Toast.LENGTH_SHORT).show()
                }, {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                })
            disposables.add(disposable)
        }

        val stockAndShareLayout = findViewById<MaterialCardView>(R.id.stockAndShareLayout)
        val planValueLabel = findViewById<MaterialCardView>(R.id.generalInvestmentAccountLayout)
        val moneyBoxLabel = findViewById<MaterialCardView>(R.id.lifetimeISALayout)

    }
}
