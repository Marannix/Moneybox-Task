package com.example.minimoneybox.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.minimoneybox.R
import com.example.minimoneybox.design.FullscreenLoadingDialog
import com.example.minimoneybox.sharedpreferences.PreferencesHelper
import com.example.minimoneybox.state.InvestorProductsViewState
import com.example.minimoneybox.viewmodel.InvestorProductsViewModel
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.saving_plan_layout.view.*
import java.util.*
import javax.inject.Inject

class DashboardFragment : BaseFragment() {

    interface OnProductsSelectedListener {
        fun onISASelected()
    }

    companion object {
        @JvmStatic
        fun newInstance() = DashboardFragment()
    }

    fun attach(listener: OnProductsSelectedListener) {
        this.listener = listener
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userPreference: PreferencesHelper
    private lateinit var loadingDialog: Dialog
    private var listener: OnProductsSelectedListener? = null

    //TODO: Remove these currency checker and place somewhere else
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadingDialog = FullscreenLoadingDialog(requireContext()).apply {
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            getProductsLayout()
        }
        subscribeToProductsViewState()
    }

    private fun getProductsLayout() {
        // View can't be null since its called after onViewCreated(Assumption)
        productsViewModel.getInvestorProductsInformation(userPreference.getToken())
        isaLayout = view!!.findViewById(R.id.stockAndShareLayout)
        giaLayout = view!!.findViewById(R.id.generalInvestmentAccountLayout)
        lisaLayout = view!!.findViewById(R.id.lifetimeISALayout)
    }

    private fun subscribeToProductsViewState() {
        productsViewModel.viewState.observe(this, Observer { productsViewState ->
            when (productsViewState) {
                InvestorProductsViewState.Loading -> {
                    loadingDialog.show()
                    Toast.makeText(requireContext(), "Loading Products", Toast.LENGTH_SHORT).show()
                }
                is InvestorProductsViewState.ShowProducts -> {
                    loadingDialog.dismiss()
                    // TODO: Improve the way I handle text
                    // TODO: Find a way to double all numbers
                    totalPlan.text =
                        "Total Plan Value: ${symbol}${String.format("%.2f", productsViewState.totalPlanValue)}"
                    setupProductsLabel(productsViewState)
                    setupProductsPlanValue(productsViewState)
                    setupProductsMoneybox(productsViewState)
                    setupListeners()
                }
                is InvestorProductsViewState.ShowError -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), "Products: $productsViewState.errorMessage", Toast.LENGTH_SHORT)
                        .show()
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
        isaLayout.planValueLabel.text = "Plan Value:"
        giaLayout.planValueLabel.text = "Plan Value:"
        lisaLayout.planValueLabel.text = "Plan Value:"

        isaLayout.planValueText.text = "${symbol}${String.format("%.2f", productsViewState.isa.planValue)}"
        giaLayout.planValueText.text = "${symbol}${String.format("%.2f", productsViewState.gia.planValue)}"
        lisaLayout.planValueText.text = "${symbol}${String.format("%.2f", productsViewState.lisa.planValue)}"
    }

    private fun setupProductsMoneybox(productsViewState: InvestorProductsViewState.ShowProducts) {
        isaLayout.moneyBoxLabel.text = "Moneybox:"
        giaLayout.moneyBoxLabel.text = "Moneybox:"
        lisaLayout.moneyBoxLabel.text = "Moneybox:"

        isaLayout.moneyBoxText.text = "${symbol}${String.format("%.2f", productsViewState.isa.moneyBox)}"
        giaLayout.moneyBoxText.text = "${symbol}${String.format("%.2f", productsViewState.gia.moneyBox)}"
        lisaLayout.moneyBoxText.text = "${symbol}${String.format("%.2f", productsViewState.lisa.moneyBox)}"
    }

    private fun setupListeners() {
        isaLayout.nextButton.setOnClickListener {
            listener?.onISASelected()
        }
    }
    
}
