package com.example.vkeducation.domain.usecase

import com.example.vkeducation.domain.entity.App
import com.example.vkeducation.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppsUseCase @Inject constructor (
    private val repository: AppRepository
) {
    operator fun invoke(): Flow<List<App>> {
        return repository.getApps()
    }
}