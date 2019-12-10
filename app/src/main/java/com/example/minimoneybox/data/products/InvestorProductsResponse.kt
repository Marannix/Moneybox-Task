package com.example.minimoneybox.data.products

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class InvestorProductsResponse(
    @SerializedName("TotalPlanValue")
    val totalPlanValue: Double,
    @SerializedName("ProductResponses")
    val productResponses: List<ProductResponses>
) :Parcelable