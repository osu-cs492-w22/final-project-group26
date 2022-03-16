package com.example.android.investaxchange.ui
/*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.investaxchange.R
import com.example.android.investaxchange.data.Asset
import com.example.android.investaxchange.data.Watchlists

class FavoritesListAdapter(private val onAssetClick: (Asset) -> Unit)
    : RecyclerView.Adapter<FavoritesListAdapter.AssetViewHolder>() {
    var watchList: List<Asset> = listOf()
    //var baseWatchlist = listOf<Watchlists>()

    fun updateWatchlists(newWatchlists: List<Asset>) {
        watchList = newWatchlists
        Log.d("Adapter", watchList.toString())
        notifyDataSetChanged()
    }

    override fun getItemCount() = watchList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.market_list_item, parent, false)
        return AssetViewHolder(itemView, onAssetClick)
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        holder.bind(watchList, position)
    }

    class AssetViewHolder(itemView: View, val onClick: (Watchlists) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        private val symbolTV : TextView = itemView.findViewById(R.id.tv_symbol)
        private val nameTV: TextView = itemView.findViewById(R.id.tv_name)
        private var currentAsset: Watchlists? = null

        init {
            itemView.setOnClickListener {
                currentAsset?.let(onClick)
                Log.d("ListAdapter", "AssetClicked!")
            }
        }

        fun bind(watchlist: Watchlists, pos: Int) {
            currentAsset = watchlist
            symbolTV.text = watchlist.assets[pos].symbol
            nameTV.text = watchlist.assets[pos].name
        }
    }
}
*/