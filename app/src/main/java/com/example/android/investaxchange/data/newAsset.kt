package com.example.android.investaxchange.data

import androidx.room.Entity
import com.squareup.moshi.Json

@Entity
data class newAsset (
    @Json(name = "symbol")
    val symbol: String
)