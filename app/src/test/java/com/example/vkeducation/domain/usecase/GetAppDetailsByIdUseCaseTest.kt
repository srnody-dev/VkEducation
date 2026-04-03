package com.example.vkeducation.domain.usecase

import com.example.vkeducation.domain.entity.AppDetails
import com.example.vkeducation.domain.entity.Category
import com.example.vkeducation.domain.repository.AppRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetAppDetailsByIdUseCaseTest {

    private val repository: AppRepository = mockk()
    private val useCase = GetAppDetailsByIdUseCase(repository)

    @Test
    fun `invoke with existing id EXPECT returns app details`() = runTest {
        val expectedDetails = AppDetails(
            id = "1",
            name = "Test App",
            developer = "Developer",
            category = Category.APP,
            ageRating = 12,
            size = 50f,
            iconUrl = "url",
            screenshotUrlList = emptyList(),
            description = "desc",
            isInWishlist = false
        )
        coEvery { repository.getAppDetailsById("1") } returns expectedDetails

        val result = useCase.invoke("1")

        assertEquals(expectedDetails, result)
    }

    @Test
    fun `invoke with non-existing id EXPECT returns null`() = runTest {
        coEvery { repository.getAppDetailsById("999") } returns null

        val result = useCase.invoke("999")

        assertNull(result)
    }

}