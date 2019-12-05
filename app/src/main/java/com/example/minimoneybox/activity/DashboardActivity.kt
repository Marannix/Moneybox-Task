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
import java.util.*
import javax.inject.Inject

class DashboardActivity : BaseActivity() {

    //TODO: Create fragment

    @Inject
    lateinit var usersRepository: UsersRepository
    @Inject
    lateinit var productsApi: ProductsApi

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //TODO: Remove these currency checker
    private val locale = Locale.getDefault()
    private val currency = Currency.getInstance(locale)
    private val symbol = currency.symbol

    private lateinit var isaLayout: MaterialCardView
    private lateinit var giaLayout: MaterialCardView
    private lateinit var lisaLayout: MaterialCardView

    private val productsViewModel: InvestorProductsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)
            .get(InvestorProductsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        if (intent.hasExtra("bearerToken")) {
            val token = intent.getStringExtra("bearerToken")
            productsViewModel.getInvestorProductsInformation("Bearer $token")
            getProductsLayout()
            setupViews()
        }
    }

    private fun getProductsLayout() {
        isaLayout = findViewById(R.id.stockAndShareLayout)
        giaLayout = findViewById(R.id.generalInvestmentAccountLayout)
        lisaLayout = findViewById(R.id.lifetimeISALayout)
    }

    private fun setupViews() {
        productsViewModel.viewState.observe(this, Observer { productsViewState ->
            when (productsViewState) {
                InvestorProductsViewState.Loading -> {
                    Toast.makeText(this, "Loading Products", Toast.LENGTH_SHORT).show()
                }
                is InvestorProductsViewState.ShowProducts -> {
                    // TODO: Improve the way I handle text
                    setupProductsLabel(productsViewState)
                    setupProductsPlanValue(productsViewState)
                    setupProductsMoneybox(productsViewState)
                }
                is InvestorProductsViewState.ShowError -> {
                    Toast.makeText(this, "Products: $productsViewState.errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupProductsLabel(productsViewState: InvestorProductsViewState.ShowProducts) {
        isaLayout.planTitle.text = productsViewState.isa.products.friendlyName
        giaLayout.planTitle.text = productsViewState.gia.products.friendlyName
        lisaLayout.planTitle.text = productsViewState.lisa.products.friendlyName
    }

    private fun setupProductsPlanValue(productsViewState: InvestorProductsViewState.ShowProducts) {
        isaLayout.planValueLabel.text = "Plan Value: ${symbol}${productsViewState.isa.planValue}"
        giaLayout.planValueLabel.text = "Plan Value: ${symbol}${productsViewState.gia.planValue}"
        lisaLayout.planValueLabel.text = "Plan Value: ${symbol}${productsViewState.lisa.planValue}"
    }

    private fun setupProductsMoneybox(productsViewState: InvestorProductsViewState.ShowProducts) {
        isaLayout.moneyBoxLabel.text = "Moneybox: ${symbol}${productsViewState.isa.moneyBox}"
        giaLayout.moneyBoxLabel.text = "Moneybox: ${symbol}${productsViewState.gia.moneyBox}"
        lisaLayout.moneyBoxLabel.text = "Moneybox: ${symbol}${productsViewState.lisa.moneyBox}"
    }

}
