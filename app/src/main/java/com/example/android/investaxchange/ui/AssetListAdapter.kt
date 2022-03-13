package com.example.android.investaxchange.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.investaxchange.R
import com.example.android.investaxchange.data.Asset

class AssetListAdapter(private val onAssetClick: (Asset) -> Unit)
    : RecyclerView.Adapter<AssetListAdapter.AssetViewHolder>() {
    var assetList = mutableListOf<Asset>()
    var baseAssetList = listOf<Asset>()

    fun updateAssetList(newAssetList: List<Asset>?) {
        baseAssetList = newAssetList ?: listOf()
        assetList = baseAssetList.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount() = assetList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.market_list_item, parent, false)
        return AssetViewHolder(itemView, onAssetClick)
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        holder.bind(assetList[position])
    }

    fun filter(text: String) {
        assetList = mutableListOf()
        if (text.isEmpty()) {
            assetList.addAll(baseAssetList)
        } else {
            val filterText = text.lowercase()
            assetList = baseAssetList.filter {
                it.name.lowercase().contains(filterText) || it.symbol.lowercase()
                    .contains(filterText)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    class AssetViewHolder(itemView: View, val onClick: (Asset) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        private val symbolTV : TextView = itemView.findViewById(R.id.tv_symbol)
        private val nameTV: TextView = itemView.findViewById(R.id.tv_name)
        private var currentAsset: Asset? = null

        init {
            itemView.setOnClickListener {
                currentAsset?.let(onClick)
            }
        }

        fun bind(asset: Asset) {
            currentAsset = asset
            symbolTV.text = asset.symbol
            nameTV.text = asset.name
        }
    }
}