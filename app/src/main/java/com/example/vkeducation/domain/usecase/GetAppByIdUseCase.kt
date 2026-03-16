package com.example.vkeducation.domain.usecase

import com.example.vkeducation.domain.entity.App
import com.example.vkeducation.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class GetAppByIdUseCase (
    private val repository: AppRepository
) {
     operator fun invoke(id:Int): Flow<App?> {
        return repository.getAppById(id)
    }
}