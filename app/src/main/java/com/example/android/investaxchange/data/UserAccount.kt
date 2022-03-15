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

    @Json(name= "currency")
    val currency: String,

    @Json(name= "equity")
    val equity: String,

    @Json(name = "cash")
    val cash: String,

    @Json(name = "long_market_value")
    val long_market_value: String,

    @Json(name = "short_market_value")
    val short_market_value: String,

    @Json(name = "pattern_day_trader")
    val pattern_day_trader: Boolean,

    @Json(name = "portfolio_value")
    val portfolio_value: String,

    @Json(name = "daytrade_count")
    val daytrade_count: Int,

    @Json(name = "account_number")
    val account_number: String,

    @Json(name = "created_at")
    val created_at: String
) : Serializable
