package com.example.vkeducation.domain.usecase

import com.example.vkeducation.domain.entity.App
import com.example.vkeducation.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class GetAppsUseCase(
    private val repository: AppRepository
) {
    operator fun invoke(): Flow<List<App>> {
        return repository.getApps()
    }
}