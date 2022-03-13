package com.example.android.investaxchange.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.investaxchange.api.AlpacaDataService
import com.example.android.investaxchange.api.AlpacaTradingService
import com.example.android.investaxchange.data.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AlpacaAssetBarsViewModel : ViewModel() {
    private val repository = AlpacaRepository(AlpacaDataService.create(), AlpacaTradingService.create())

    private val _searchResults = MutableLiveData<BarsResponse?>(null)
    val searchResults: LiveData<BarsResponse?> = _searchResults

    private val _loadingStatus = MutableLiveData(LoadingStatus.SUCCESS)
//    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    fun loadBars(symbol: String) {
        viewModelScope.launch {
            val endMillis = System.currentTimeMillis() - 15 * 60 * 1000;
            val startMillis = endMillis - 7 * 24 * 60 * 60 * 1000;

            val endDate = Date(endMillis);
            val startDate = Date(startMillis)

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            sdf.timeZone = TimeZone.getTimeZone("GMT")

            val endTime = sdf.format(endDate)
            val startTime = sdf.format(startDate)

            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadBars(symbol, startTime, endTime)
            //Log.d("PortfolioModel", result.toString())
            _searchResults.value = result.getOrNull()
//            _loadingStatus.value = when (result.isSuccess) {
//                true ->  LoadingStatus.SUCCESS
//                false -> LoadingStatus.ERROR
//            }
        }
    }
}