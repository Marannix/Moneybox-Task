package com.example.minimoneybox.fragment

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.minimoneybox.R
import com.example.minimoneybox.data.products.ProductResponses
import com.example.minimoneybox.design.FullscreenLoadingDialog
import com.example.minimoneybox.state.InvestedMoneyboxViewState
import com.example.minimoneybox.utils.PriceUtils
import com.example.minimoneybox.utils.TEN_POUND_PRICE
import com.example.minimoneybox.viewmodel.InvestorProductsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_investment.*


private const val ARG_PRODUCTS = "products"

class InvestmentFragment : BaseFragment() {

    private lateinit var products: ProductResponses
    private lateinit var viewmodel: InvestorProductsViewModel
    private lateinit var loadingDialog: Dialog

    companion object {
        @JvmStatic
        fun newInstance(products: ProductResponses) = InvestmentFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_PRODUCTS, products)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_investment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            products = it.getParcelable(ARG_PRODUCTS)!!
        }
        loadingDialog = FullscreenLoadingDialog(requireContext()).apply {
            setCanceledOnTouchOutside(false)
        }
    }
    
    override fun onStart() {
        super.onStart()
        viewmodel =
            activity?.let { ViewModelProviders.of(it, viewModelFactory).get(InvestorProductsViewModel::class.java) }!!
        display()
        subscribeToViewState()
        setPaymentListener()
    }

    private fun display() {
        investmentName.text = products.products.friendlyName

        investmentPlanValueLabel.text = getString(R.string.total_plan_label)
        investmentMoneyBoxLabel.text = getString(R.string.money_box_label)

        investmentPlanValue.text = PriceUtils.calculatePriceString(products.planValue)
        investmentMoneyBox.text = PriceUtils.calculatePriceString(products.moneyBox)

    }

    private fun setPaymentListener() {
        investmentAddMoney.setOnClickListener {
            viewmodel.makePayment(userPreference.getToken(), products.moneyBox, TEN_POUND_PRICE, products.id)
        }
    }

    private fun subscribeToViewState() {
        viewmodel.paymentViewState.observe(this, Observer { viewstate ->
            when (viewstate) {
                is InvestedMoneyboxViewState.Loading -> {
                    loadingDialog.show()
                }
                is InvestedMoneyboxViewState.ShowUpdatedMoneyBox -> {
                    viewmodel.paymentViewState.postValue(null)
                    viewmodel.getInvestorProductsInformation(userPreference.getToken())
                    returnToDashboard()
                }
                is InvestedMoneyboxViewState.ShowError -> {
                    loadingDialog.dismiss()
                    Snackbar.make(this.view!!, viewstate.errorMessage.toString(), Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        })
    }

    private fun returnToDashboard() {
        // Added a delay to simulate updating
        val handler = Handler()
        handler.postDelayed({ loadingDialog.dismiss()}, 3000)
        activity!!.onBackPressed()
    }

}
