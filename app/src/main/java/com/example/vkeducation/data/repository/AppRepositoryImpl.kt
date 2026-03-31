package com.example.vkeducation.data.repository

import android.util.Log
import com.example.vkeducation.data.local.AppsDao
import com.example.vkeducation.data.mapper.toAppDetails
import com.example.vkeducation.data.mapper.toAppDetailsDb
import com.example.vkeducation.data.mapper.toAppShortDb
import com.example.vkeducation.data.mapper.toAppDetailsDomain
import com.example.vkeducation.data.mapper.toAppDomain
import com.example.vkeducation.data.remote.AppsApiService
import com.example.vkeducation.domain.entity.AppDetails
import com.example.vkeducation.domain.entity.AppShort
import com.example.vkeducation.domain.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AppRepositoryImpl @Inject constructor(
    private val appsDao: AppsDao,
    private val appsApiService: AppsApiService
) : AppRepository {

    companion object {
        private const val TAG = "AppRepository"
        private const val CACHE = 5 * 60 * 1000
    }

    override fun getApps(): Flow<List<AppShort>> {
        return appsDao.getApp()
            .map { list -> list.map { it.toAppDomain() } }
            .onStart {
                val needUpdate = checkIfNeedUpdate()
                if (needUpdate) {
                    Log.d(TAG, "Cache outdated or empty, loading from API...")
                    loadAppsFromApiAndSave()
                } else Log.d(TAG, "Cache is fresh, using cached data")
            }
            .catch { e ->
                if (e is CancellationException) throw e
                Log.e(TAG, "Error in getApps", e)
                emit(emptyList())
            }
    }

    override suspend fun getAppDetailsById(id: String): AppDetails? {

        val entity = appsDao.getAppDetailsById(id).first()

        if (entity != null) {
            Log.d(TAG, "App details for $id found in cache")
            return entity.toAppDetailsDomain()
        }

        Log.d(TAG, "App details for $id not in cache, loading from API")
        return try {
            val remoteDetails = appsApiService.getAppById(id)
            val dbModel = remoteDetails.toAppDetailsDb()

            appsDao.addAppDetails(dbModel)
            remoteDetails.toAppDetails()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e(TAG, "Error loading app details for $id", e)
            null
        }
    }

    override suspend fun refreshApps() {
        Log.d(TAG, "Brutal Force refreshing apps")
        loadAppsFromApiAndSave()
    }

    private suspend fun checkIfNeedUpdate(): Boolean {

        val lastUpdateTime = appsDao.getLastUpdateTime()
        val appsCount = appsDao.getAppsCount()

        if (appsCount == 0) {
            Log.d(TAG, "No data in cache, need update")
            return true
        }

        if (lastUpdateTime != null) {
            val timePassed = System.currentTimeMillis() - lastUpdateTime
            if (timePassed > CACHE) {
                Log.d(
                    TAG,
                    "Cache outdated (${timePassed / 1000 / 60} minutes old), need update"
                )
                return true
            }
        }
        return false
    }

    private suspend fun loadAppsFromApiAndSave() {
        try {
            val apps = appsApiService.loadApps()
            if (apps.isNotEmpty()) {
                val currentTime = System.currentTimeMillis()
                val dbModels = apps.map {
                    it.toAppShortDb().copy(lastUpdated = currentTime)
                }
                appsDao.refreshApps(dbModels)
                Log.d(TAG, "Successfully loaded and saved ${apps.size} apps")
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e(TAG, "Failed to load apps from API", e)
        }
    }

    override fun observeAppDetails(id: String): Flow<AppDetails?> {
        return appsDao.getAppDetailsById(id)
            .map { entity ->
                if (entity != null) {
                    Log.d(TAG, "observeAppDetails: App details for $id found in cache")
                    entity.toAppDetailsDomain()
                } else {
                    Log.d(TAG, "observeAppDetails: App details for $id not in cache")
                    null
                }
            }
    }

    override suspend fun toggleWishlist(id: String) {
        try {
            appsDao.toggleWishlistStatus(id)
            Log.d(TAG, "Wishlist toggled for $id")
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e(TAG, "Error toggling wishlist for $id", e)
        }

    }

}