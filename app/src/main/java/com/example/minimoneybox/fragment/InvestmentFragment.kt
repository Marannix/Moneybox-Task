package com.example.minimoneybox.fragment

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.minimoneybox.data.products.ProductResponses
import com.example.minimoneybox.design.FullscreenLoadingDialog
import com.example.minimoneybox.state.InvestedMoneyboxViewState
import com.example.minimoneybox.utils.PriceUtils
import com.example.minimoneybox.viewmodel.InvestorProductsViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            products = it.getParcelable(ARG_PRODUCTS)!!
        }
        loadingDialog = FullscreenLoadingDialog(requireContext()).apply {
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.example.minimoneybox.R.layout.fragment_investment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel =
            activity?.let { ViewModelProviders.of(it, viewModelFactory).get(InvestorProductsViewModel::class.java) }!!
        display()
        subscribeToViewState()
        setPaymentListener()
    }

    private fun display() {
        investmentName.text = products.products.friendlyName

        investmentPlanValueLabel.text = "Plan Value:"
        investmentMoneyBoxLabel.text = "Money Box:"

        investmentPlanValue.text = PriceUtils.calculatePriceString(products.planValue)
        investmentMoneyBox.text = PriceUtils.calculatePriceString(products.moneyBox)

    }

    private fun setPaymentListener() {
        investmentAddMoney.setOnClickListener {
            viewmodel.makePayment(userPreference.getToken(), 10, products.id)
        }
    }

    private fun subscribeToViewState() {
        viewmodel.paymentViewState.observe(this, Observer { viewstate ->
            when (viewstate) {
                is InvestedMoneyboxViewState.Loading -> {
                    loadingDialog.show()
                    Toast.makeText(requireContext(), "Making payment", Toast.LENGTH_SHORT).show()
                }
                is InvestedMoneyboxViewState.ShowUpdatedMoneyBox -> {
                    returnToDashboard()
                }
                is InvestedMoneyboxViewState.ShowError -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), viewstate.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun returnToDashboard() {
        // Added a delay to simulate updating
        val handler = Handler()
        handler.postDelayed({ loadingDialog.dismiss()}, 4000)
        // TODO: Fix current bug, unable to go back into InvestmentFragment
        activity!!.supportFragmentManager.popBackStack()
    }

}
