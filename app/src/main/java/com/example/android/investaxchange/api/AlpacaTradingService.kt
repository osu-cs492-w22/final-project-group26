package com.example.android.investaxchange.api

import com.example.android.investaxchange.BuildConfig
import com.example.android.investaxchange.data.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface AlpacaTradingService {
    /**
     * Get a list of available assets.
     */
    @GET("/v2/assets")
    suspend fun getAssets() : List<Asset>

    /**
     * Get list of stocks account currently owns
     */
    @GET("/v2/positions")
    suspend fun getPortfolioAssets() : List<PortfolioAssets>

    /**
     * Get a list of all assets being watched on the account
     */
    @GET("/v2/watchlists/7aba2d78-c838-40f1-a60b-39e11c3557c3")
    suspend fun getWatchlists() : Watchlists

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

    /**
     * Create a new order.
     */
    @POST("/v2/orders")
    suspend fun createOrder(@Body order: OrderRequest) : Order

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

    /**
     * Add an asset to the main watchlist for cloud storage
     */
    @POST("/v2/watchlists/7aba2d78-c838-40f1-a60b-39e11c3557c3")
    suspend fun createFavorite(@Body newasset: newAsset): Response<ResponseBody>

    /**
     * Remove an asset from the watchlist for completely syncronous local/cloud storage
     */
    @DELETE("/v2/watchlists/7aba2d78-c838-40f1-a60b-39e11c3557c3/{symbol}")
    suspend fun removeFavorite(@Path("symbol") symbol: String): Response<ResponseBody>
}