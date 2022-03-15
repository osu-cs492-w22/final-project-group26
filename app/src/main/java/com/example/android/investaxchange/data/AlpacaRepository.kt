package com.example.android.investaxchange.data

import com.example.android.investaxchange.api.AlpacaDataService
import com.example.android.investaxchange.api.AlpacaTradingService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class AlpacaRepository(
    private val dataService: AlpacaDataService,
    private val tradingService: AlpacaTradingService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadAssets(): Result<List<Asset>> =
        withContext(ioDispatcher) {
            try {
                val results = tradingService.getAssets()
                Result.success(results)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun loadSnapshot(symbol: String): Result<Snapshot> =
        withContext(ioDispatcher) {
            try {
                val results = dataService.getSnapshot(symbol)
                Result.success(results)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun loadSnapshots(symbols: List<String>): Result<Map<String, Snapshot>> =
        withContext(ioDispatcher) {
            try {
                val results = dataService.getSnapshots(symbols.joinToString(","))
                Result.success(results)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun loadBars(symbol: String, start: String, end: String): Result<BarsResponse> =
        withContext(ioDispatcher) {
            try {
                val results = dataService.getBars(symbol, start, end)
                Result.success(results)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun loadPortfolioHistory(period: String, timeframe: String, extendedHours: Boolean): Result<PortfolioHistory> =
        withContext(ioDispatcher) {
            try {
                val results = tradingService.getPortfolioHistory(period, timeframe, extendedHours)
                Result.success(results)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun loadPortfolioAssets(): Result<List<PortfolioAssets>> =
        withContext(ioDispatcher) {
            try {
                val results = tradingService.getPortfolioAssets()
                Result.success(results)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
//    private fun buildAccountQuery(
//        query: String,
//        user: String?,
//        languages: Set<String>?,
//        firstIssues: Int
//    ) : String {
//        /*
//         * e.g. "android user:square language:kotlin language:java good-first-issues:>=2"
//         */
//        var fullQuery = query
//        if (!TextUtils.isEmpty(user)) {
//            fullQuery += " user:$user"
//        }
//        if (languages != null && languages.isNotEmpty()) {
//            fullQuery += languages.joinToString(separator = " language:", prefix = "language:")
//        }
//        if (firstIssues > 0) {
//            fullQuery += " good-first-issues:>=$firstIssues"
//        }
//        return fullQuery
//    }
}