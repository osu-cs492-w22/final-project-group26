package com.example.android.investaxchange.api

import com.example.android.investaxchange.BuildConfig
import com.example.android.investaxchange.data.Asset
import com.example.android.investaxchange.data.PortfolioAssets
import com.example.android.investaxchange.data.PortfolioHistory
import com.example.android.investaxchange.data.UserAccount
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AlpacaTradingService {
    /**
     * Get a list of available assets.
     */
    @GET("v2/assets")
    suspend fun getAssets() : List<Asset>

    /**
     * Get list of stocks account currently owns
     */

    @GET("v2/positions")
    suspend fun getPortfolioAssets() : List<PortfolioAssets>

    /**
     * Get lists with data for portfolio history.
     */
    @GET("/v2/account/portfolio/history")
    suspend fun getPortfolioHistory(
        @Query("period") period: String,
        @Query("timeframe") timeframe: String,
        @Query("extended_hours") extended_hours: Boolean) : PortfolioHistory

    /**
     * Get information with data for User Account.
     */
    @GET("/v2/account")
    suspend fun getUserAccount() : UserAccount

    companion object {
        private const val BASE_URL = "https://paper-api.alpaca.markets/"

        fun create() : AlpacaTradingService {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("APCA-API-KEY-ID", BuildConfig.KEYID)
                        .addHeader("APCA-API-SECRET-KEY", BuildConfig.SECRETID)
                        .build()

                    chain.proceed(request)
                }
                .build()
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(client)
                .build()
            return retrofit.create(AlpacaTradingService::class.java)
        }
    }
}