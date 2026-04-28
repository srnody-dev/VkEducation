package com.example.vkeducation.domain.usecase

import com.example.vkeducation.domain.entity.AppDetails
import com.example.vkeducation.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppDetailsByIdUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend operator fun invoke(id: String): AppDetails? {
        return repository.getAppDetailsById(id)
    }
}