package com.example.android.investaxchange.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.android.investaxchange.R
import com.example.android.investaxchange.data.PortfolioAssets
import com.google.android.material.card.MaterialCardView

class PortfolioAssetListAdapter(private val onAssetClick: (PortfolioAssets) -> Unit)
    : RecyclerView.Adapter<PortfolioAssetListAdapter.AssetViewHolder>() {
    var portfolioAssetList: List<PortfolioAssets> = listOf()

    fun updateAssetList(newPortfolioAssetList: List<PortfolioAssets>?) {
        portfolioAssetList = newPortfolioAssetList ?: listOf()
//        portfolioAssetList = (newPortfolioAssetList ?: listOf()).sortedByDescending { it.qty.toInt() * it.current_price.toDouble() }
        notifyDataSetChanged()
    }

    override fun getItemCount() = this.portfolioAssetList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.portfolio_list_item, parent, false)
        return AssetViewHolder(itemView, onAssetClick)
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        holder.bind(portfolioAssetList[position])
    }

    class AssetViewHolder(itemView: View, val onClick: (PortfolioAssets) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        private val cardTV : MaterialCardView = itemView.findViewById(R.id.portfolio_card)
        private val symbolTV : TextView = itemView.findViewById(R.id.tv_portfolio_asset_symbol)
//        private val nameTV: TextView = itemView.findViewById(R.id.tv_portfolio_asset_name)
        private val qtyTV: TextView = itemView.findViewById(R.id.tv_portfolio_asset_qty)
        private val avgpriceTV: TextView = itemView.findViewById(R.id.tv_portfolio_asset_avgprice)
        private val priceTV: TextView = itemView.findViewById(R.id.tv_portfolio_asset_price)
        private val diffTV: TextView = itemView.findViewById(R.id.tv_portfolio_diff)
        private var currentAsset: PortfolioAssets? = null

        init {
            itemView.setOnClickListener {
                currentAsset?.let(onClick)
            }
        }

        fun bind(asset: PortfolioAssets) {
            currentAsset = asset
            symbolTV.text = asset.symbol
//            nameTV.text = "None"
            qtyTV.text = asset.qty
            avgpriceTV.text = String.format("%.2f", asset.avg_entry_price.toDouble())
            priceTV.text = String.format("%.2f", asset.current_price.toDouble())

            val diff = (asset.current_price.toDouble() - asset.avg_entry_price.toDouble()) * asset.qty.toDouble()

            if (diff < 0) {
                diffTV.text = String.format("%.2f", diff)
                diffTV.setTextColor(Color.RED)
            } else if (diff > 0) {
                diffTV.text = String.format("+%.2f", diff)
                diffTV.setTextColor(Color.argb(250, 0, 187, 90))
            } else {
                diffTV.setTextColor(Color.GRAY)
            }
        }
    }
}