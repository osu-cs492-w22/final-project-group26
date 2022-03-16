package com.example.android.investaxchange.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.investaxchange.api.AlpacaDataService
import com.example.android.investaxchange.api.AlpacaTradingService
import com.example.android.investaxchange.data.AlpacaRepository
import com.example.android.investaxchange.data.Asset
import com.example.android.investaxchange.data.LoadingStatus
import com.example.android.investaxchange.data.Watchlists
import kotlinx.coroutines.launch

class AlpacaFavoritesViewModel : ViewModel() {
    private val repository = AlpacaRepository(AlpacaDataService.create(), AlpacaTradingService.create())

    private val _watchlists = MutableLiveData<List<Asset>?>(null)
    val watchlists: LiveData<List<Asset>?> = _watchlists

    private val _loadingStatus = MutableLiveData(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    fun loadWatchlists() {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadWatchlists()
            Log.d("ViewModel", result.toString())
            _watchlists.value = result.getOrNull()
            _loadingStatus.value = when (result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
        }
    }
}