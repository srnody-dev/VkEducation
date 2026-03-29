package com.example.vkeducation.di

import android.content.Context
import androidx.room.Room
import com.example.vkeducation.data.local.AppsDao
import com.example.vkeducation.data.local.AppsDatabase
import com.example.vkeducation.data.remote.AppsApiService
import com.example.vkeducation.data.repository.AppRepositoryImpl
import com.example.vkeducation.domain.repository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindsRepository(repositoryImpl: AppRepositoryImpl): AppRepository

    companion object {

        @Provides
        @Singleton
        fun providesAppsDatabase(@ApplicationContext context: Context): AppsDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = AppsDatabase::class.java,
                name = "appShorts.db"
            ).fallbackToDestructiveMigration(dropAllTables = true).build()
        }

        @Provides
        @Singleton
        fun providesNewsDao(database: AppsDatabase): AppsDao {
            return database.appsDao()
        }

        @Provides
        @Singleton
        fun providesApiService(
            retrofit: Retrofit
        ): AppsApiService {
            return retrofit.create(AppsApiService::class.java)
        }

        @Provides
        @Singleton
        fun provideJson(): Json {
            return Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
        }

        @Provides
        @Singleton
        fun provideConverterFactory(
            json: Json
        ): Converter.Factory {
            return json.asConverterFactory("application/json".toMediaType())
        }

        @Provides
        @Singleton
        fun provideRetrofit(
            converterFactory: Converter.Factory
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://185.103.109.134/")
                .addConverterFactory(converterFactory)
                .build()
        }
    }
}