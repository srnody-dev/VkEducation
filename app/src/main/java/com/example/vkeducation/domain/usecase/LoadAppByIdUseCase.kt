package com.example.vkeducation.domain.usecase

import com.example.vkeducation.domain.entity.App
import com.example.vkeducation.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadAppByIdUseCase @Inject constructor(
    private val repository: AppRepository
) {
    operator fun invoke(id: String): Flow<App?> {
        return repository.loadAppById(id)
    }
}