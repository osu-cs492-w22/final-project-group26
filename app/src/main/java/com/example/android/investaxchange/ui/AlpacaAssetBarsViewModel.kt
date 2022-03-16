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

    private val _bars = MutableLiveData<List<Bar>?>(null)
    val bars: LiveData<List<Bar>?> = _bars

    private val _loadingStatus = MutableLiveData(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    fun loadBars(symbol: String, days: Int, n_datapoints: Int) {
        viewModelScope.launch {
            val endMillis = System.currentTimeMillis() - 15 * 60 * 1000L;
            val startMillis = endMillis - days * 24 * 60 * 60 * 1000L;

            val endDate = Date(endMillis);
            val startDate = Date(startMillis)

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            sdf.timeZone = TimeZone.getTimeZone("GMT")

            val endTime = sdf.format(endDate)
            val startTime = sdf.format(startDate)

            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadBars(symbol, startTime, endTime, n_datapoints)
            _bars.value = result.getOrNull()?.bars
            _loadingStatus.value = when (result.isSuccess) {
                true ->  LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
        }
    }
}