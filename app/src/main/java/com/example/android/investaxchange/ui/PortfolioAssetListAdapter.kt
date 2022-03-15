package com.example.android.investaxchange.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.investaxchange.R
import com.example.android.investaxchange.data.PortfolioAssets

class PortfolioAssetListAdapter(private val onAssetClick: (PortfolioAssets) -> Unit)
    : RecyclerView.Adapter<PortfolioAssetListAdapter.AssetViewHolder>() {
    var portfolioAssetList: List<PortfolioAssets> = listOf()
    fun updateAssetList(newPortfolioAssetList: List<PortfolioAssets>?) {
        portfolioAssetList = newPortfolioAssetList?: listOf()
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
        private val symbolTV : TextView = itemView.findViewById(R.id.tv_portfolio_asset_symbol)
//        private val nameTV: TextView = itemView.findViewById(R.id.tv_portfolio_asset_name)
        private val qtyTV: TextView = itemView.findViewById(R.id.tv_portfolio_asset_qty)
        private val avgpriceTV: TextView = itemView.findViewById(R.id.tv_portfolio_asset_avgprice)
        private val priceTV: TextView = itemView.findViewById(R.id.tv_portfolio_asset_price)
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
            avgpriceTV.text = asset.avg_entry_price
            priceTV.text = asset.current_price
        }
    }
}