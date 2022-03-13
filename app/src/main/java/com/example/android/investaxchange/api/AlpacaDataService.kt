package com.example.android.investaxchange.api

import com.example.android.investaxchange.BuildConfig
import com.example.android.investaxchange.data.Snapshot
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AlpacaDataService {
    /**
     * Retrieve a snapshot of a specific symbol.
     *
     * @param symbol the symbol to retrieve the snapshot of
     */
    @GET("v2/stocks/{symbol}/snapshot")
    suspend fun getSnapshot(@Path("symbol") symbol: String) : Snapshot

    /**
     * Retrieve snapshots of multiple symbols.
     *
     * @param symbols the symbols (comma-separated) to retrieve snapshots of
     */
    @GET("v2/stocks/snapshots")
    suspend fun getSnapshots(@Query("symbols") symbols: String) : Map<String, Snapshot>

    companion object {
        private const val BASE_URL = "https://data.alpaca.markets/"

        fun create() : AlpacaDataService {
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
            return retrofit.create(AlpacaDataService::class.java)
        }
    }
}