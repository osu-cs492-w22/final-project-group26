package com.example.android.investaxchange.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity
data class UserAccount(
    @Json(name = "id")
    @PrimaryKey
    val id: String,

    @Json(name = "status")
    val status: String,

    @Json(name = "cash")
    val cash: Int,

    @Json(name = "portfolio_value")
    val portfolio_value: Int
) : Serializable
