package com.example.android.investaxchange.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.investaxchange.R
import com.example.android.investaxchange.data.Asset
import com.example.android.investaxchange.data.LoadingStatus
import com.google.android.material.progressindicator.CircularProgressIndicator

class MarketFragment : Fragment(R.layout.market) {
    private val TAG = "MarketFragment"

    private val assetListAdapter = AssetListAdapter(::onAssetClick)
    private val viewModel: AlpacaAssetsViewModel by viewModels()

    private lateinit var searchBoxET: EditText
    private lateinit var searchResultsListRV: RecyclerView
    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBoxET = view.findViewById(R.id.et_search_box)
        searchResultsListRV = view.findViewById(R.id.rv_search_results)
        searchErrorTV = view.findViewById(R.id.tv_search_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        searchResultsListRV.layoutManager = LinearLayoutManager(requireContext())
        searchResultsListRV.setHasFixedSize(true)

        searchResultsListRV.adapter = assetListAdapter

        viewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            assetListAdapter.updateAssetList(searchResults)
        }

        viewModel.loadingStatus.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                LoadingStatus.LOADING -> {
                    loadingIndicator.visibility = View.VISIBLE
                    searchResultsListRV.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    loadingIndicator.visibility = View.INVISIBLE
                    searchResultsListRV.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.VISIBLE
                }
                else -> {
                    loadingIndicator.visibility = View.INVISIBLE
                    searchResultsListRV.visibility = View.VISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.loadAssets()

        searchBoxET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                assetListAdapter.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun onAssetClick(asset: Asset) {
        val directions = MarketFragmentDirections.navigateToMarketDetail(asset)
        findNavController().navigate(directions)
    }
}