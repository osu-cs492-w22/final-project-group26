package com.example.android.investaxchange.api

import okhttp3.Interceptor

class OAuthInterceptor(private val keyID: String, private val secretID: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder()
            .header("APCA-API-KEY-ID", "Bearer $keyID")
            .addHeader("APCA-API-SECRET-KEY", "Bearer $secretID")
            .build()

        return chain.proceed(request)
    }
}