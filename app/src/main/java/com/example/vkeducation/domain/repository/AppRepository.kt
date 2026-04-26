package com.example.vkeducation.domain.repository

import com.example.vkeducation.domain.entity.App
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun loadApps(): Flow<List<App>>
    fun loadAppById(id: String): Flow<App?>
}