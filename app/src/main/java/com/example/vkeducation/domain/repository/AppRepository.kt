package com.example.vkeducation.domain.repository

import com.example.vkeducation.domain.entity.AppDetails
import com.example.vkeducation.domain.entity.AppShort
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getApps(): Flow<List<AppShort>>

    suspend fun getAppDetailsById(id: String): AppDetails?

    suspend fun refreshApps()
}