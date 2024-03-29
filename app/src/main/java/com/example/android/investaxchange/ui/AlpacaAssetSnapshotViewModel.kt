package com.example.android.investaxchange.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.investaxchange.api.AlpacaDataService
import com.example.android.investaxchange.api.AlpacaTradingService
import com.example.android.investaxchange.data.AlpacaRepository
import com.example.android.investaxchange.data.LoadingStatus
import com.example.android.investaxchange.data.Snapshot
import kotlinx.coroutines.launch

class AlpacaAssetSnapshotViewModel : ViewModel() {
    private val repository = AlpacaRepository(AlpacaDataService.create(), AlpacaTradingService.create())

    private val _snapshot = MutableLiveData<Snapshot?>(null)
    val snapshot: LiveData<Snapshot?> = _snapshot

    private val _loadingStatus = MutableLiveData(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    fun loadSnapshot(symbol: String) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadSnapshot(symbol)
            _snapshot.value = result.getOrNull()
            _loadingStatus.value = when (result.isSuccess) {
                true ->  LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
        }
    }
}