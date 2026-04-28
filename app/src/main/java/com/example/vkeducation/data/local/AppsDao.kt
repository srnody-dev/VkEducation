package com.example.vkeducation.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

}