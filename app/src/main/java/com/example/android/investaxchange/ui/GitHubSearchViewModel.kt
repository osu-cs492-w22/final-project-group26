package com.example.android.investaxchange.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.investaxchange.data.GitHubRepo
import com.example.android.investaxchange.data.GitHubReposRepository
import com.example.android.investaxchange.api.AlpacaService
import com.example.android.investaxchange.data.LoadingStatus
import kotlinx.coroutines.launch

class GitHubSearchViewModel : ViewModel() {
    private val repository = GitHubReposRepository(AlpacaService.create())

    private val _searchResults = MutableLiveData<List<GitHubRepo>?>(null)
    val searchResults: LiveData<List<GitHubRepo>?> = _searchResults

    private val _loadingStatus = MutableLiveData(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    fun loadSearchResults(
        query: String,
        sort: String?,
        user: String?,
        languages: Set<String>?,
        firstIssues: Int
    ) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadRepositoriesSearch(query, sort, user, languages, firstIssues)
            _searchResults.value = result.getOrNull()
            _loadingStatus.value = when (result.isSuccess) {
                true ->  LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
        }
    }
}