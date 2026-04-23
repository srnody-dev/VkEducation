package com.example.vkeducation.di

import com.example.vkeducation.data.local.LocalSourceImpl
import com.example.vkeducation.data.repository.AppRepositoryImpl
import com.example.vkeducation.domain.repository.AppRepository
import com.example.vkeducation.domain.usecase.GetAppByIdUseCase
import com.example.vkeducation.domain.usecase.GetAppsUseCase

object DataModule {
    private val localSource = LocalSourceImpl()
    private val appRepository: AppRepository = AppRepositoryImpl(localSource)

    val getAppsUseCase: GetAppsUseCase = GetAppsUseCase(appRepository)
    val getAppByIdUseCase: GetAppByIdUseCase = GetAppByIdUseCase(appRepository)
}