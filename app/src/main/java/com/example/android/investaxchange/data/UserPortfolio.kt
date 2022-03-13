package com.example.android.investaxchange.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity
data class PortfolioHistory(
    @Json(name = "timestamp")
    @PrimaryKey
    val timeStamp: List<Int>,

    @Json(name = "equity")
    val equity: List<Double>,

    @Json(name = "profit_loss")
    val profit_loss: List<Double>,

    @Json(name = "profit_loss_pct")
    val profit_loss_pct: List<Float>,

    @Json(name = "base_value")
    val base_value: Double,

    @Json(name = "timeframe")
    val timeframe: String
) : Serializable
