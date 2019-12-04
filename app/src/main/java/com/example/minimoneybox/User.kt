package com.example.minimoneybox

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("FirstName")
    val firstName: String
) : Parcelable