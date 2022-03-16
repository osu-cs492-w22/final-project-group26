package com.example.android.investaxchange.data

class FavoriteAssets(
    private val dao: AssetDao
) {
    suspend fun insertAsset(asset: Asset) = dao.insert(asset)
    suspend fun removeAsset(asset: Asset) = dao.delete(asset)
    fun getAllAssets() = dao.getAllAssets()
    fun getAssetByName(name: String) = dao.getAssetByName(name)
    fun assetExists(id: String): Boolean = dao.exists(id)
}