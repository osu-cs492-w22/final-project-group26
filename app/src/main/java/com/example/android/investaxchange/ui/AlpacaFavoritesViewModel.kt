package com.example.android.investaxchange.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.investaxchange.api.AlpacaDataService
import com.example.android.investaxchange.api.AlpacaTradingService
import com.example.android.investaxchange.data.*
import kotlinx.coroutines.launch

class AlpacaFavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AlpacaRepository(AlpacaDataService.create(), AlpacaTradingService.create())
    private val database = FavoriteAssets(AppDatabase.getInstance(getApplication()).assetDao())

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

    fun addAsset(asset: Asset){
        viewModelScope.launch {
            database.insertAsset(asset)
        }
    }

    fun removeAsset(asset: Asset){
        viewModelScope.launch {
            database.removeAsset(asset)
        }
    }

    fun assetExists(assetId: String): Boolean {
        var res: Boolean = false
        viewModelScope.launch {
            res = database.assetExists(assetId)
        }
        return res
    }
}