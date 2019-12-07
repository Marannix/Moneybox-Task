package com.example.minimoneybox.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.minimoneybox.R
import com.example.minimoneybox.data.products.ProductResponses
import com.example.minimoneybox.utils.PriceUtils
import kotlinx.android.synthetic.main.fragment_investment.*

private const val ARG_PRODUCTS = "products"

class InvestmentFragment : BaseFragment() {

    private var products: ProductResponses? = null

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
            products = it.getParcelable(ARG_PRODUCTS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_investment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        display()
    }

    private fun display() {
        investmentName.text = products!!.products.friendlyName

        investmentPlanValueLabel.text = "Plan Value:"
        investmentMoneyBoxLabel.text = "Money Box:"

        investmentPlanValue.text = PriceUtils.calculatePriceString(products!!.planValue)
        investmentMoneyBox.text = PriceUtils.calculatePriceString(products!!.moneyBox)
    }

}
