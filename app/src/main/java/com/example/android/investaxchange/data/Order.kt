package com.example.android.investaxchange.data

import com.squareup.moshi.Json

data class OrderRequest(
    @Json(name = "symbol")
    val symbol: String,

    @Json(name = "qty")
    val qty: Int,

    @Json(name = "side")
    val side: String,

    @Json(name = "type")
    val type: String,

    @Json(name = "time_in_force")
    val timeInForce: String,

    @Json(name = "limit_price")
    val limitPrice: Double? = null,

    @Json(name = "extended_hours")
    val extendedHours: Boolean? = null,
)

data class Order(
    @Json(name = "id")
    val id: String,
)