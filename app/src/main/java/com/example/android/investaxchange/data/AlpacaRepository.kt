package com.example.android.investaxchange.data

import com.example.android.investaxchange.api.AlpacaService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class AlpacaRepository(
    private val service: AlpacaService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadAccount(): Result<UserAccount> =
        withContext(ioDispatcher) {
            try {
                val results = service.getAccount()
                Result.success(results.item)
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