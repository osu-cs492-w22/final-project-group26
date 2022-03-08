package com.example.android.investaxchange.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity
data class GitHubRepo(
    @Json(name = "full_name")
    @PrimaryKey
    val name: String,

    val description: String,

    @Json(name = "html_url")
    val url: String,

    @Json(name = "stargazers_count")
    val stars: Int
) : Serializable
