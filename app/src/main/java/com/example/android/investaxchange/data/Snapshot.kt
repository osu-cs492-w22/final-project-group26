package com.example.android.investaxchange.data

import com.squareup.moshi.Json
import java.io.Serializable

data class Snapshot(
    @Json(name = "latestTrade")
    val latestTrade: Trade,

    @Json(name = "latestQuote")
    val latestQuote: Quote,

    @Json(name = "minuteBar")
    val minuteBar: Bar,

    @Json(name = "dailyBar")
    val dailyBar: Bar,

    @Json(name = "prevDailyBar")
    val prevDailyBar: Bar,
) : Serializable

data class Trade(
    @Json(name = "t")
    val time: String,

    @Json(name = "x")
    val exchange: String,

    @Json(name = "p")
    val price: Double,

    @Json(name = "s")
    val size: Int,

    @Json(name = "c")
    val conditions: List<String>,

    @Json(name = "i")
    val id: Long,

    @Json(name = "z")
    val tape: String,
)

data class Quote(
    @Json(name = "t")
    val time: String,

    @Json(name = "ax")
    val askExchange: String,

    @Json(name = "ap")
    val askPrice: Double,

    @Json(name = "as")
    val askSize: Int,

    @Json(name = "bx")
    val bidExchange: String,

    @Json(name = "bp")
    val bidPrice: Double,

    @Json(name = "bs")
    val bidSize: Int,

    @Json(name = "c")
    val conditions: List<String>,
)
