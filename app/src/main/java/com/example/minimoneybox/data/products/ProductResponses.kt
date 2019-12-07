package com.example.minimoneybox.data.products

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductResponses(
    @SerializedName("Id")
    val id : Int,
    @SerializedName("PlanValue")
    val planValue: Double,
    @SerializedName("Moneybox")
    val moneyBox: Double,
    @SerializedName("Product")
    val products: Products
) : Parcelable
