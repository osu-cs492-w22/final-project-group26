package com.example.android.investaxchange.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.investaxchange.R
import com.example.android.investaxchange.data.GitHubRepo
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import com.tradingview.lightweightcharts.api.chart.models.color.toIntColor
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.enums.TrackingModeExitMode
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.api.series.enums.LastPriceAnimationMode
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.HistogramData
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.api.series.models.Time
import com.tradingview.lightweightcharts.api.series.models.WhitespaceData
import com.tradingview.lightweightcharts.runtime.plugins.DateTimeFormat
import com.tradingview.lightweightcharts.runtime.plugins.PriceFormatter
import com.tradingview.lightweightcharts.runtime.plugins.TimeFormatter
import com.tradingview.lightweightcharts.view.ChartsView
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(R.layout.home) {
    private val TAG = "HomeFragment"

//    private val repoListAdapter = GitHubRepoListAdapter(::onGitHubRepoClick)
    private val viewModel: AlpacaPortfolioViewModel by viewModels()
    private val chartsView get() = requireView().findViewById<ChartsView>(R.id.charts_view)

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
                            view.findViewById<TextView>(R.id.portfolio_value).text = "$${String.format("%.2f", searchResult.equity.last())}"
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




        //val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

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
    }

    private fun onGitHubRepoClick(repo: GitHubRepo) {
//        val directions = GitHubSearchFragmentDirections.navigateToRepoDetail(repo, 16)
//        findNavController().navigate(directions)
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
                borderColor = Color.argb(255, 197, 203, 206).toIntColor()
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