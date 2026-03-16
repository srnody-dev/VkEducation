package com.example.vkeducation.domain.repository

import com.example.vkeducation.domain.entity.App
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getApps(): Flow<List<App>>
    fun getAppById(id: Int): Flow<App?>
}