package com.example.minimoneybox.activity

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.minimoneybox.R
import com.example.minimoneybox.api.ProductsApi
import com.example.minimoneybox.repository.UsersRepository
import com.example.minimoneybox.state.InvestorProductsViewState
import com.example.minimoneybox.viewmodel.InvestorProductsViewModel
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.saving_plan_layout.view.*
import javax.inject.Inject

class DashboardActivity : BaseActivity() {

    //TODO: Create fragment and retrieve user information from the database

    @Inject
    lateinit var usersRepository: UsersRepository
    @Inject
    lateinit var productsApi: ProductsApi

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val productsViewModel: InvestorProductsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)
            .get(InvestorProductsViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        if (intent.hasExtra("bearerToken")) {
            val token = intent.getStringExtra("bearerToken")
//            val disposable = productsApi.getInvestorProducts("Bearer $token")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    Toast.makeText(this, it.productResponses[0].id.toString(), Toast.LENGTH_SHORT).show()
//                }, {
//                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
//                })
//            disposables.add(disposable)

            productsViewModel.getInvestorProductsInformation("Bearer $token")
            setupViews()
        }

    }

    private fun setupViews() {
        productsViewModel.viewState.observe(this, Observer { productsViewState ->
            when (productsViewState) {
                InvestorProductsViewState.Loading -> {
                    Toast.makeText(this, "Loading Products", Toast.LENGTH_SHORT).show()
                }
                is InvestorProductsViewState.ShowProducts -> {
                    val isaLayout = findViewById<MaterialCardView>(R.id.stockAndShareLayout)
                    val giaLayout = findViewById<MaterialCardView>(R.id.generalInvestmentAccountLayout)
                    val lisaLayout = findViewById<MaterialCardView>(R.id.lifetimeISALayout)

                    isaLayout.planTitle.text = productsViewState.isa.products.friendlyName
                    giaLayout.planTitle.text = productsViewState.gia.products.friendlyName
                    lisaLayout.planTitle.text = productsViewState.lisa.products.friendlyName
                }
                is InvestorProductsViewState.ShowError -> {
                    Toast.makeText(this, "Products: $productsViewState.errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
