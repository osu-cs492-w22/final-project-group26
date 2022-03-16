package com.example.android.investaxchange.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity
data class Watchlists (
    @Json(name = "id")
    @PrimaryKey
    val listId: String,

    @Json(name = "account_id")
    val accountId: String,

    @Json(name = "created_at")
    val created: String,

    @Json(name = "updated_at")
    val updated: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "assets")
    val assets: List<Asset>
) : Serializable