package com.example.vkeducation.domain.usecase

import com.example.vkeducation.domain.repository.AppRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ToggleWishlistUseCaseTest {

    private val repository: AppRepository = mockk()
    private val useCase = ToggleWishlistUseCase(repository)

    @Test
    fun `invoke with id EXPECT calls repository toggleWishlist with correct id`() = runTest {

        coEvery { repository.toggleWishlist("app_123") } returns Unit

        useCase.invoke("app_123")

        coVerify(exactly = 1) { repository.toggleWishlist("app_123") }
    }

    @Test
    fun `invoke with different ids EXPECT calls repository with each id`() = runTest {

        coEvery { repository.toggleWishlist(any()) } returns Unit
        useCase.invoke("app_1")
        useCase.invoke("app_2")
        useCase.invoke("app_3")

        coVerify(exactly = 1) { repository.toggleWishlist("app_1") }
        coVerify(exactly = 1) { repository.toggleWishlist("app_2") }
        coVerify(exactly = 1) { repository.toggleWishlist("app_3") }
    }
}