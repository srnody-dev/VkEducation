package com.example.vkeducation.domain.usecase

import com.example.vkeducation.domain.repository.AppRepository
import javax.inject.Inject

class RefreshAppsUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend operator fun invoke() {
        return repository.refreshApps()
    }
}