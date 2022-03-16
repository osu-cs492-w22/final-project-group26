package com.example.android.investaxchange.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.example.android.investaxchange.R
import com.example.android.investaxchange.data.OrderRequest
import com.google.android.material.snackbar.Snackbar
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import com.tradingview.lightweightcharts.api.chart.models.color.toIntColor
import com.tradingview.lightweightcharts.api.options.enums.TrackingModeExitMode
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.HistogramData
import com.tradingview.lightweightcharts.api.series.models.Time
import com.tradingview.lightweightcharts.view.ChartsView
import java.text.SimpleDateFormat
import java.util.*

class MarketDetailFragment : Fragment(R.layout.market_detail) {
    private val args: MarketDetailFragmentArgs by navArgs()

    private val barsViewModel: AlpacaAssetBarsViewModel by viewModels()
    private val snapshotViewModel: AlpacaAssetSnapshotViewModel by viewModels()
    private val orderViewModel: AlpacaOrderViewModel by viewModels()
    private val favoriteViewModel: AlpacaFavoritesViewModel by viewModels()

    private val chartsView get() = requireView().findViewById<ChartsView>(R.id.charts_view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        view.findViewById<TextView>(R.id.tv_market_name).text = args.asset.symbol
        view.findViewById<TextView>(R.id.tv_market_description).text = args.asset.name

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        var n_datapoints = sharedPrefs.getInt(
            getString(R.string.pref_datapoint_key),
            1
        )

        if (n_datapoints == 0)
            n_datapoints = 1

        barsViewModel.loadBars(args.asset.symbol, 30, n_datapoints)
        snapshotViewModel.loadSnapshot(args.asset.symbol)
        snapshotViewModel.snapshot.observe(viewLifecycleOwner) {
            if (it?.latestTrade?.price == null) {
                view.findViewById<TextView>(R.id.tv_market_price).text = "Loading..."
            } else {
                view.findViewById<TextView>(R.id.tv_market_price).text = "$${String.format("%.2f", it.latestTrade.price)}"
            }
        }

        chartsView.subscribeOnChartStateChange { state ->
            when (state) {
                is ChartsView.State.Preparing -> Unit
                is ChartsView.State.Ready -> {
                    barsViewModel.bars.observe(viewLifecycleOwner) {
                        if (it != null) {
                            val data = it.map { bar ->
                                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                                val date: Date = format.parse(bar.time)

                                HistogramData(Time.Utc.fromDate(date), bar.closePrice.toFloat())
                            }

                            chartsView.api.addAreaSeries(
                                AreaSeriesOptions(
                                    topColor = "rgba(33, 150, 243, 0.56)".toIntColor(),
                                    bottomColor = "rgba(33, 150, 243, 0.04)".toIntColor(),
                                    lineColor = "rgba(33, 150, 243, 1)".toIntColor(),
                                    lineWidth = LineWidth.FOUR
                                ),
                                onSeriesCreated = { series ->
                                    series.setData(data)
                                }
                            )
                        }
                    }

                    applyChartOptions()
                }
                is ChartsView.State.Error -> {
                    Log.d("HomeFragment", state.exception.message ?: "")
                }
            }
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.title = args.asset.symbol

        val buy1Button = view.findViewById<Button>(R.id.buy_1_button)
        val buy10Button = view.findViewById<Button>(R.id.buy_10_button)
        val buy50Button = view.findViewById<Button>(R.id.buy_50_button)

        orderViewModel.order.observe(viewLifecycleOwner) {
            if (it != null) {
                val snackbar =
                    Snackbar.make(requireView(), "Order ${it.id} submitted!", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }

        buy1Button.setOnClickListener { orderViewModel.createOrder(getOrderRequest(1)) }
        buy10Button.setOnClickListener { orderViewModel.createOrder(getOrderRequest(10)) }
        buy50Button.setOnClickListener { orderViewModel.createOrder(getOrderRequest(50)) }
    }

    private fun getOrderRequest(qty: Int): OrderRequest {
        val basePrice = snapshotViewModel.snapshot.value?.latestTrade?.price ?: 0.0
        val limitPrice = basePrice * when {
            basePrice <= 25.00 -> 1.1
            basePrice <= 50.00 -> 1.05
            else -> 1.03
        }

        return OrderRequest(
            symbol = args.asset.symbol,
            qty = qty,
            side = "buy",
            type = "limit",
            limitPrice = limitPrice,
            timeInForce = "day",
            extendedHours = true,
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.activity_market_detail, menu)
        if (favoriteViewModel.assetExists(args.asset.id)) {
            var fav: MenuItem = menu.getItem(0)
            fav.setIcon(R.drawable.ic_action_favorite_on)
            fav.isChecked = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_share -> {
                shareMarketDetail()
                true
            }
            R.id.action_favorite -> {
                if (item.isChecked) {
                    item.setIcon(R.drawable.ic_action_favorite_off)
                    item.isChecked = false
                } else {
                    item.setIcon(R.drawable.ic_action_favorite_on)
                    item.isChecked = true
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareMarketDetail() {
        val price = snapshotViewModel.snapshot.value?.latestTrade?.price ?: 0

        val text = getString(R.string.share_text, args.asset.symbol, "%.2f".format(price.toDouble()))
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, null))
    }

    private fun applyChartOptions() {
        chartsView.api.applyOptions {
            handleScale = handleScaleOptions {
                kineticScroll = kineticScrollOptions {
                    touch = false
                    mouse = false
                }
            }
            layout = layoutOptions {
                background = SolidColor(Color.WHITE)
                textColor = Color.argb(255, 33, 56, 77).toIntColor()
            }
            crosshair = crosshairOptions {
                mode = CrosshairMode.NORMAL
            }
            rightPriceScale = priceScaleOptions {
                scaleMargins = PriceScaleMargins(
                    top = 0.35f,
                    bottom = 0.2f
                )
                borderColor = Color.argb(255, 197, 203, 206).toIntColor()
            }
            timeScale = timeScaleOptions {
                fixRightEdge = true
                borderColor = Color.argb(255, 197, 203, 206).toIntColor()
                timeVisible = true
            }
            trackingMode = TrackingModeOptions(exitMode = TrackingModeExitMode.ON_TOUCH_END)
        }
    }
}