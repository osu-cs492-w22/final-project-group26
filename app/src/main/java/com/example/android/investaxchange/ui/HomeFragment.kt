package com.example.android.investaxchange.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.investaxchange.R
import com.example.android.investaxchange.data.GitHubRepo
import com.example.android.investaxchange.data.LoadingStatus
import com.example.android.investaxchange.data.PortfolioAssets
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import com.tradingview.lightweightcharts.api.chart.models.color.toIntColor
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.enums.TrackingModeExitMode
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.HistogramData
import com.tradingview.lightweightcharts.api.series.models.Time
import com.tradingview.lightweightcharts.view.ChartsView
import java.util.*

class HomeFragment : Fragment(R.layout.home) {
    private val TAG = "HomeFragment"

    private val portfolioAssetListAdapter = PortfolioAssetListAdapter(::onPorfolioAssetClick)
    private val viewModel: AlpacaPortfolioViewModel by viewModels()
    private val viewModel2: AlpacaPortfolioAssetsViewModel by viewModels()
    private val chartsView get() = requireView().findViewById<ChartsView>(R.id.charts_view)

    private lateinit var searchResultsListRV: RecyclerView
    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var series: SeriesApi

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chartsView.api.addAreaSeries(
            AreaSeriesOptions(
                topColor = "rgba(33, 150, 243, 0.56)".toIntColor(),
                bottomColor = "rgba(33, 150, 243, 0.04)".toIntColor(),
                lineColor = "rgba(33, 150, 243, 1)".toIntColor(),
                lineWidth = LineWidth.FOUR
            ),
            onSeriesCreated = { series = it }
        )

        /*
        *
        * Creates a chart with newly updated results from portfolio
        *
        */
        chartsView.subscribeOnChartStateChange { state ->
            when (state) {
                is ChartsView.State.Preparing -> Unit
                is ChartsView.State.Ready -> {
                    viewModel.searchResults.observe(viewLifecycleOwner) { searchResult ->
                        if (searchResult != null) {
                            val mapData: Map< Long, Float> =
                                searchResult.timeStamp
                                    .zip(searchResult.equity)
                                    .toMap()

                            val histogramData: List<HistogramData> = mapData.filter {
                                it.value != null
                            }.map {
                                val date = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                                date.timeInMillis = it.key * 1000L
                                HistogramData(Time.Utc.fromDate(date.time), it.value)
                            }

                            series.setData(histogramData)
                            view.findViewById<TextView>(R.id.portfolio_value).text = "$${String.format("%.2f", histogramData.last().value)}"
                        }
                        else {
                            view.findViewById<TextView>(R.id.portfolio_value).text = "Loading..."
                        }
                    }

                    applyChartOptions()
                    viewModel.loadAccountResult("1D", "15Min", true)
                }
                is ChartsView.State.Error -> {
                    Log.d("HomeFragment", state.exception.message ?: "")
                }
            }
        }

        val searchBtn1: Button = view.findViewById(R.id.home_chart_Day)
        searchBtn1.setOnClickListener {
            viewModel.loadAccountResult("1D", "15Min", true)
        }
        val searchBtn2: Button = view.findViewById(R.id.home_chart_Week)
        searchBtn2.setOnClickListener {
            viewModel.loadAccountResult("1W", "1H", true)
        }
        val searchBtn3: Button = view.findViewById(R.id.home_chart_Month)
        searchBtn3.setOnClickListener {
            viewModel.loadAccountResult("1M", "1D", true)
        }

        searchResultsListRV = view.findViewById(R.id.rv_portfolio_asset_results)
        searchErrorTV = view.findViewById(R.id.tv_portfolio_asset_error)
        loadingIndicator = view.findViewById(R.id.portfolio_asset_loading_indicator)

        searchResultsListRV.layoutManager = LinearLayoutManager(requireContext())
        searchResultsListRV.setHasFixedSize(true)

        searchResultsListRV.adapter = portfolioAssetListAdapter

        viewModel2.portfolioAssets.observe(viewLifecycleOwner) { searchResults ->
            portfolioAssetListAdapter.updateAssetList(searchResults)
        }

        viewModel2.loadingStatus.observe(viewLifecycleOwner) { uiState ->
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

        viewModel2.loadPortfolioAssets()
    }

    private fun onPorfolioAssetClick(portfolioAssets: PortfolioAssets) {
        val directions = HomeFragmentDirections.navigateToPortfolioAssetDetail(portfolioAssets)
        findNavController().navigate(directions)
    }

    private fun applyChartOptions() {
        chartsView.api.applyOptions {
            handleScale = handleScaleOptions {
                kineticScroll = kineticScrollOptions {
                    touch = true
                    mouse = true
                }
            }
            handleScroll = handleScrollOptions {
                mouseWheel = true
                pressedMouseMove = true
                horzTouchDrag = true
            }
            layout = layoutOptions {
                background = SolidColor(Color.WHITE)
                textColor = Color.argb(255, 33, 56, 77).toIntColor()
            }
            crosshair = crosshairOptions {
                mode = CrosshairMode.MAGNET
            }
            rightPriceScale = priceScaleOptions {
                scaleMargins = PriceScaleMargins(
                    top = 0.35f,
                    bottom = 0.2f
                )
                borderColor = Color.argb(0, 197, 203, 206).toIntColor()
            }
            timeScale = timeScaleOptions {
                //fixRightEdge = true
                borderColor = Color.argb(255, 197, 203, 206).toIntColor()
                barSpacing = 50.0f
                minBarSpacing = 6.0f
            }
            trackingMode = TrackingModeOptions(exitMode = TrackingModeExitMode.ON_TOUCH_END)
        }
    }
}