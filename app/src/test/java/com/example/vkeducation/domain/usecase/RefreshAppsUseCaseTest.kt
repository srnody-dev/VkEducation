package com.example.vkeducation.domain.usecase

import com.example.vkeducation.domain.repository.AppRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class RefreshAppsUseCaseTest {
    private val repository: AppRepository = mockk()
    private val useCase = RefreshAppsUseCase(repository)

    @Test
    fun `invoke EXPECT calls repository refreshApps`() = runTest {
        coEvery { repository.refreshApps() } returns Unit
        useCase.invoke()

        coVerify(exactly = 1) { repository.refreshApps() }
    }

    @Test
    fun `invoke when called multiple times EXPECT multiple repository calls`() = runTest {

        coEvery { repository.refreshApps() } returns Unit
        useCase.invoke()
        useCase.invoke()
        useCase.invoke()

        coVerify(exactly = 3) { repository.refreshApps() }
    }
}