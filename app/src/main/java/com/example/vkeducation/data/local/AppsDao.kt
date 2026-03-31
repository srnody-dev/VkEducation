package com.example.vkeducation.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow


@Dao
interface AppsDao {

    @Query("SELECT * FROM appShorts")
    fun getApp(): Flow<List<AppShortDbModel>>

    @Query("SELECT * FROM appDetails WHERE id = :id")
    fun getAppDetailsById(id: String): Flow<AppDetailsDbModel?>

    @Query("SELECT MAX(lastUpdated) FROM appShorts")
    suspend fun getLastUpdateTime(): Long?

    @Query("SELECT COUNT(*) FROM appShorts")
    suspend fun getAppsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addApps(apps: List<AppShortDbModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAppDetails(app: AppDetailsDbModel)

    @Query("DELETE FROM appShorts")
    suspend fun deleteAllApps()

    @Query("UPDATE appDetails SET isInWishlist = NOT isInWishlist WHERE id = :id")
    suspend fun toggleWishlistStatus(id: String)

    @Transaction
    suspend fun refreshApps(apps: List<AppShortDbModel>){
        deleteAllApps()
        addApps(apps)
    }

}