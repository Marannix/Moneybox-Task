package com.example.minimoneybox.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object PriceUtils {
    fun calculatePriceString(price: Double) : String {
        val locale = Locale.getDefault()
        val decimalFormat = DecimalFormat("0.00", DecimalFormatSymbols(locale))
        val currency = Currency.getInstance(locale)
        return currency.symbol + decimalFormat.format(price).toString()
    }
}