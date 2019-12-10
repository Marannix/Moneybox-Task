package com.example.minimoneybox.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

const val TEN_POUND_PRICE = 10

object PriceUtils {
    fun calculatePriceString(price: Double) : String {
        val locale = Locale.getDefault()
        val currency = Currency.getInstance(locale)
        return currency.symbol + formatDecimal().format(price).toString()
    }

    fun formatDecimal(): DecimalFormat {
        return DecimalFormat("0.00", DecimalFormatSymbols(Locale.getDefault()))
    }
}