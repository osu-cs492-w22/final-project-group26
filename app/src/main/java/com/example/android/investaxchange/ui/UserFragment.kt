package com.example.android.investaxchange.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.example.android.investaxchange.R
import com.example.android.investaxchange.data.LoadingStatus
import com.example.android.investaxchange.data.UserAccount
import com.google.android.material.progressindicator.CircularProgressIndicator
import org.w3c.dom.Text


class UserFragment : Fragment(R.layout.fragment_user) {
    private val TAG = "UserFragment"
    private val viewModel: AlpacaAccountViewModel by viewModels()

    private lateinit var anyChartBar: AnyChartView
    private lateinit var anyChartPie: AnyChartView
    private lateinit var tvAccountNumber: TextView
    private lateinit var tvCreatedAt: TextView

    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        anyChartBar = view.findViewById(R.id.any_chart_bar)
        anyChartPie = view.findViewById(R.id.any_chart_pie)
        tvAccountNumber = view.findViewById(R.id.tv_user_account_number)
        tvCreatedAt = view.findViewById(R.id.tv_user_created_at)

        viewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            searchResults?.let {
                tvAccountNumber.text = "Account ID: " + searchResults?.account_number
                tvCreatedAt.text = "Created on: " + searchResults?.created_at.take(10)
                setupBarChart(searchResults!!)
                setupPieChart(searchResults!!)
            }
        }
        viewModel.loadAccountResult();
    }

    private fun setupBarChart(searchResults: UserAccount) {
        APIlib.getInstance().setActiveAnyChartView(anyChartBar)
        val cartesian = AnyChart.column()

        val data: MutableList<DataEntry> = mutableListOf()
        data.add(ValueDataEntry("Cash", searchResults.cash.toFloat()))
        data.add(ValueDataEntry("EQTY", searchResults.equity.toFloat()))
        data.add(ValueDataEntry("PF", searchResults.portfolio_value.toFloat()))
        data.add(ValueDataEntry("SM", searchResults.short_market_value.toFloat()))
        data.add(ValueDataEntry("LM", searchResults.long_market_value.toFloat()))

        val column = cartesian.column(data)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("\${%Value}{groupsSeparator: }")

        cartesian.animation(true);
        cartesian.title("Account Overview");
        cartesian.yScale().minimum(0.0)
        cartesian.yAxis(0).labels().format("\${%Value}{groupsSeparator: }")
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.xAxis(0).title("Fund Overview");
        cartesian.yAxis(0).title(searchResults.currency);
        anyChartBar.setChart(cartesian);
    }

    private fun setupPieChart(searchResults: UserAccount) {
        APIlib.getInstance().setActiveAnyChartView(anyChartPie)
        val pie = AnyChart.pie()
        val data: MutableList<DataEntry> = mutableListOf()
        data.add(ValueDataEntry("Long Market Value", searchResults.long_market_value.toFloat()))
        data.add(ValueDataEntry("Short Market Value", searchResults.short_market_value.toFloat()))
        data.add(ValueDataEntry("Cash", searchResults.cash.toFloat()))
        pie.data(data)
        pie.title("Equity Breakdown")
        anyChartPie.setChart(pie)
    }
}