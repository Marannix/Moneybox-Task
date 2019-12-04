package com.example.minimoneybox

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "user")
data class User(
    // TODO: Check if userid can be used as primary key, hopefully yes, else I'll have to generate a fake id
    @PrimaryKey(autoGenerate = false)
    @SerializedName("UserId")
    val userId: String,
    @SerializedName("FirstName")
    val firstName: String
) : Parcelable