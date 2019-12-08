package com.example.minimoneybox.data.products

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "products")
data class ProductResponses(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("Id")
    val id : Int,
    @SerializedName("PlanValue")
    val planValue: Double,
    @SerializedName("Moneybox")
    val moneyBox: Double,
    @SerializedName("Product")
    @Embedded(prefix = "product_")
    val products: Products
) : Parcelable
