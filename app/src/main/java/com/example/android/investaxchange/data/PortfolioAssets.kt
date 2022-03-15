package com.example.android.investaxchange.data

import com.squareup.moshi.Json
import java.io.Serializable

data class PortfolioAssets(
    @Json(name = "asset_id")
    val asset_id: String,

    @Json(name = "symbol")
    val symbol: String,

    @Json(name = "exchange")
    val exchange: String,

    @Json(name = "asset_class")
    val asset_class: String,

//    @Json(name = "asset_marginable")
//    val asset_marginable: Boolean,

    @Json(name = "qty")
    val qty: String,

    @Json(name = "avg_entry_price")
    val avg_entry_price: String,

//    @Json(name = "side")
//    val side: String,
//
//    @Json(name = "market_value")
//    val market_value: String,
//
//    @Json(name = "unrealized_pl")
//    val unrealized_pl: String,
//
//    @Json(name = "unrealized_plpc")
//    val unrealized_plpc: String,
//
//    @Json(name = "unrealized_intraday_pl")
//    val unrealized_intraday_pl: String,
//
//    @Json(name = "unrealized_intraday_plpc")
//    val unrealized_intraday_plpc: String,

    @Json(name = "current_price")
    val current_price: String,

    @Json(name = "lastday_price")
    val lastday_price: String,

    @Json(name = "change_today")
    val change_today: String,
) : Serializable
