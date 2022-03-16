package com.example.android.investaxchange.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asset: Asset)

    @Delete
    suspend fun delete(asset: Asset)

    @Query("SELECT * FROM Asset")
    fun getAllAssets(): Flow<List<Asset>>

    @Query("SELECT * FROM Asset WHERE name = :name LIMIT 1")
    fun getAssetByName(name: String): Flow<Asset?>

    @Query("SELECT EXISTS (SELECT 1 FROM Asset WHERE id = :id)")
    fun exists(id: String): Boolean

    @Query("SELECT EXISTS (SELECT 1 FROM Asset WHERE symbol = :sym)")
    fun symbolExists(sym: String): Boolean
}