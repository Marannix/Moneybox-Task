package com.example.minimoneybox.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.minimoneybox.R


class InvestmentFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = InvestmentFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_investment, container, false)
    }

}
