package com.example.minimoneybox.data.products

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Products(
    val id: Int,
    @SerializedName("FriendlyName")
    val friendlyName: String
) : Parcelable
