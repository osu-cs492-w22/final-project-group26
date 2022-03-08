package com.example.android.investaxchange.api

import com.example.android.investaxchange.data.GitHubSearchResults
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// TODO: replace with alpaca-java library
interface AlpacaService {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String? = "stars"
    ) : GitHubSearchResults

    companion object {
        private const val BASE_URL = "https://data.alpaca.market/v2/"
        fun create() : AlpacaService {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
            return retrofit.create(AlpacaService::class.java)
        }
    }
}