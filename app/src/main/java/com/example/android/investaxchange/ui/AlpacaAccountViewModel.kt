package com.example.android.investaxchange.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.investaxchange.api.AlpacaDataService
import com.example.android.investaxchange.api.AlpacaTradingService
import com.example.android.investaxchange.data.*
import kotlinx.coroutines.launch

class AlpacaAccountViewModel : ViewModel() {
    private val repository = AlpacaRepository(AlpacaDataService.create(), AlpacaTradingService.create())

    private val _searchResults = MutableLiveData<UserAccount?>(null)
    val searchResults: LiveData<UserAccount?> = _searchResults

    private val _loadingStatus = MutableLiveData(LoadingStatus.SUCCESS)
//    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    fun loadAccountResult() {
        viewModelScope.launch {
            repository.loadAssets()

            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadSnapshot("AAPL")
            _searchResults.value = result.getOrNull()
            _loadingStatus.value = when (result.isSuccess) {
                true ->  LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
        }
    }
}