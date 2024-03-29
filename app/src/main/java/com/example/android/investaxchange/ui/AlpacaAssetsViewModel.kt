package com.example.android.investaxchange.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.investaxchange.api.AlpacaDataService
import com.example.android.investaxchange.api.AlpacaTradingService
import com.example.android.investaxchange.data.AlpacaRepository
import com.example.android.investaxchange.data.Asset
import com.example.android.investaxchange.data.LoadingStatus
import kotlinx.coroutines.launch

class AlpacaAssetsViewModel : ViewModel() {
    private val repository = AlpacaRepository(AlpacaDataService.create(), AlpacaTradingService.create())

    private val _assets = MutableLiveData<List<Asset>?>(null)
    val assets: LiveData<List<Asset>?> = _assets

    private val _loadingStatus = MutableLiveData(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    fun loadAssets() {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadAssets()
            _assets.value = result.getOrNull()
            _loadingStatus.value = when (result.isSuccess) {
                true ->  LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
        }
    }
}