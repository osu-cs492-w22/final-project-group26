package com.example.android.investaxchange.api

import com.example.android.investaxchange.BuildConfig
import com.example.android.investaxchange.data.UserAccountResults
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


const val KEYID = BuildConfig.KEYID
const val SECRETID = BuildConfig.SECRETID
// TODO: replace with alpaca-java library
interface AlpacaService {
    @GET("v2/account")
    suspend fun getAccount() : UserAccountResults

    companion object {
        private const val BASE_URL = "https://paper-api.alpaca.markets/"
        fun create() : AlpacaService {
            val client = OkHttpClient.Builder()
                .addInterceptor(OAuthInterceptor(KEYID, SECRETID))
                .build()
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(client)
                .build()
            return retrofit.create(AlpacaService::class.java)
        }
    }
}