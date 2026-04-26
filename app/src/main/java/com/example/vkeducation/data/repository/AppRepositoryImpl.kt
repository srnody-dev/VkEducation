package com.example.vkeducation.data.repository

import android.util.Log
import com.example.vkeducation.data.mapper.toDomain
import com.example.vkeducation.data.remote.AppsApiService
import com.example.vkeducation.domain.entity.App
import com.example.vkeducation.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val appsApiService: AppsApiService
) : AppRepository {

    override fun loadApps(): Flow<List<App>> = flow {

        Log.d("AppRepository", "Loading apps from API")
        val apps = appsApiService.loadApps().toDomain()
        Log.d("AppRepository", "Loaded ${apps.size} apps successfully")
        emit(apps)
    }.catch { e ->
        Log.e("AppRepository", "Failed to load apps", e)
        emit(emptyList())
    }


    override fun loadAppById(id: String): Flow<App?> = flow {
        Log.d("AppRepository", "Loading app by id: $id")
        val app = appsApiService.getAppById(id).toDomain()
        Log.d("AppRepository", "Loaded app: ${app.name} (id: $id)")
        emit(app)
    }.catch { e ->
        Log.e("AppRepository", "Failed to load app with id: $id", e)
        throw e
    }
}