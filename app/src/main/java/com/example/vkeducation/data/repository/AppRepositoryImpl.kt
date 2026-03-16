package com.example.vkeducation.data.repository

import android.util.Log
import com.example.vkeducation.data.local.LocalSource
import com.example.vkeducation.data.mapper.toDomain
import com.example.vkeducation.domain.entity.App
import com.example.vkeducation.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppRepositoryImpl(
    private val localSource: LocalSource
) : AppRepository {

    override fun getApps(): Flow<List<App>> = flow {
        try {
            val appsDto = localSource.getApps()
            val appsDomain = appsDto.toDomain()
            emit(appsDomain)
        } catch (e: Exception) {
            Log.e("AppRepository", "Failed to get apps: ${e.message}")
            emit(emptyList())
        }
    }

    override fun getAppById(id: Int): Flow<App?> = flow {
        try {
            val appDto = localSource.getAppById(id)
            if (appDto != null) {
                val appDomain = appDto.toDomain()
                emit(appDomain)
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Failed to get app by id $id: ${e.message}")
            emit(null)
        }
    }
}