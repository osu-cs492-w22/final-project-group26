package com.example.android.investaxchange.data

import com.squareup.moshi.Json
import java.io.Serializable

data class BarsResponse(
    @Json(name = "bars")
    val bars: List<Bar>,

    @Json(name = "symbol")
    val symbol: String,
) : Serializable

data class Bar(
    @Json(name = "t")
    val time: String,

    @Json(name = "o")
    val openPrice: Double,

    @Json(name = "h")
    val highPrice: Double,

    @Json(name = "l")
    val lowPrice: Double,

    @Json(name = "c")
    val closePrice: Double,

    @Json(name = "v")
    val volume: Int,
)