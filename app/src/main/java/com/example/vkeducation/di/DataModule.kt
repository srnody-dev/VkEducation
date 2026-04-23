package com.example.vkeducation.di

import com.example.vkeducation.data.local.LocalSource
import com.example.vkeducation.data.local.LocalSourceImpl
import com.example.vkeducation.data.repository.AppRepositoryImpl
import com.example.vkeducation.domain.repository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindsRepository(repositoryImpl: AppRepositoryImpl): AppRepository

    @Binds
    @Singleton
    fun bindsLocalSource(localImpl: LocalSourceImpl): LocalSource
}