package com.example.minimoneybox.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.minimoneybox.R
import com.example.minimoneybox.data.products.ProductResponses
import com.example.minimoneybox.design.FullscreenLoadingDialog
import com.example.minimoneybox.state.InvestorProductsViewState
import com.example.minimoneybox.utils.PriceUtils
import com.example.minimoneybox.viewmodel.InvestorProductsViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.saving_plan_layout.view.*

class DashboardFragment : BaseFragment() {

    interface OnProductsSelectedListener {
        fun onIsaSelected(isa: ProductResponses)
        fun onGiaSelected(gia: ProductResponses)
        fun onLisaSelected(lisa: ProductResponses)
    }

    companion object {
        @JvmStatic
        fun newInstance() = DashboardFragment()
    }

    fun attach(listener: OnProductsSelectedListener) {
        this.listener = listener
    }

    private lateinit var loadingDialog: Dialog
    private var listener: OnProductsSelectedListener? = null
    private var viewmodel: InvestorProductsViewModel? = null
    private lateinit var isaLayout: MaterialCardView
    private lateinit var giaLayout: MaterialCardView
    private lateinit var lisaLayout: MaterialCardView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    private fun init() {
        loadingDialog = FullscreenLoadingDialog(requireContext()).apply {
            setCanceledOnTouchOutside(false)
        }
        viewmodel =
            activity?.let { ViewModelProviders.of(it, viewModelFactory).get(InvestorProductsViewModel::class.java) }
        findLayouts()
        getProducts()
        subscribeToProductsViewState()
        setUserFullName()
    }

    private fun setUserFullName() {
        if (userPreference.getUserFullName().isNotEmpty()) {
            chosenName.text = getString(R.string.hello_user_full_name, userPreference.getUserFullName())
        }
    }

    private fun getProducts() {
        viewmodel!!.getInvestorProductsInformation(userPreference.getToken())
    }

    private fun findLayouts() {
        isaLayout = view!!.findViewById(R.id.stockAndShareLayout)
        giaLayout = view!!.findViewById(R.id.generalInvestmentAccountLayout)
        lisaLayout = view!!.findViewById(R.id.lifetimeISALayout)
    }

    private fun subscribeToProductsViewState() {
        viewmodel!!.viewState.observe(this, Observer { productsViewState ->
            when (productsViewState) {
                InvestorProductsViewState.Loading -> {
                    loadingDialog.show()
                }
                is InvestorProductsViewState.ShowProducts -> {
                    loadingDialog.dismiss()
                    // TODO: Improve the way I handle text
                    totalPlan.text = getString(
                        R.string.total_plan_label,
                        PriceUtils.calculatePriceString(productsViewState.totalPlanValue)
                    )
                    setupProductsLabel(productsViewState)
                    setupProductsPlanValue(productsViewState)
                    setupProductsMoneybox(productsViewState)
                    setupListeners(productsViewState)
                }
                is InvestorProductsViewState.ShowError -> {
                    loadingDialog.dismiss()
                    if (productsViewState.errorCode == 401) {
                        userPreference.setUserHasLoggedIn(false)
                        Snackbar.make(this.view!!, getString(productsViewState.errorMessage), Snackbar.LENGTH_LONG)
                            .show()
                    } else {
                        Snackbar.make(this.view!!, "Products: $productsViewState.errorMessage", Snackbar.LENGTH_LONG)
                            .show()
                    }

                }
                is InvestorProductsViewState.ShowUnknownError -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), productsViewState.errorMessage, Toast.LENGTH_SHORT).show()
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
        isaLayout.planValueLabel.text = getString(R.string.plan_value_label)
        giaLayout.planValueLabel.text = getString(R.string.plan_value_label)
        lisaLayout.planValueLabel.text = getString(R.string.plan_value_label)

        isaLayout.planValueText.text = PriceUtils.calculatePriceString(productsViewState.isa.planValue)
        giaLayout.planValueText.text = PriceUtils.calculatePriceString(productsViewState.gia.planValue)
        lisaLayout.planValueText.text = PriceUtils.calculatePriceString(productsViewState.lisa.planValue)
    }

    private fun setupProductsMoneybox(productsViewState: InvestorProductsViewState.ShowProducts) {
        isaLayout.moneyBoxLabel.text = getString(R.string.money_box_label)
        giaLayout.moneyBoxLabel.text = getString(R.string.money_box_label)
        lisaLayout.moneyBoxLabel.text = getString(R.string.money_box_label)

        isaLayout.moneyBoxText.text = PriceUtils.calculatePriceString(productsViewState.isa.moneyBox)
        giaLayout.moneyBoxText.text = PriceUtils.calculatePriceString(productsViewState.gia.moneyBox)
        lisaLayout.moneyBoxText.text = PriceUtils.calculatePriceString(productsViewState.lisa.moneyBox)
    }

    private fun setupListeners(productsViewState: InvestorProductsViewState.ShowProducts) {
        isaLayout.nextButton.setOnClickListener {
            listener?.onIsaSelected(productsViewState.isa)
        }
        giaLayout.nextButton.setOnClickListener {
            listener?.onGiaSelected(productsViewState.gia)
        }
        lisaLayout.nextButton.setOnClickListener {
            listener?.onLisaSelected(productsViewState.lisa)
        }
    }
}
