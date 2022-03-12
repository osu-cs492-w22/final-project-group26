package com.example.android.investaxchange.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity
data class Asset(
    @Json(name = "id")
    @PrimaryKey
    val id: String,

    @Json(name = "class")
    val assetClass: String,

    @Json(name = "exchange")
    val exchange: String,

    @Json(name = "symbol")
    val symbol: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "status")
    val status: String,

    @Json(name = "tradable")
    val tradable: Boolean,

    @Json(name = "marginable")
    val marginable: Boolean,

    @Json(name = "shortable")
    val shortable: Boolean,

    @Json(name = "fractionable")
    val fractionable: Boolean,

    @Json(name = "easy_to_borrow")
    val easyToBorrow: Boolean,
) : Serializable
