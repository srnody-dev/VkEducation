package com.example.vkeducation.domain.usecase

import com.example.vkeducation.domain.entity.AppShort
import com.example.vkeducation.domain.entity.Category
import com.example.vkeducation.domain.repository.AppRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAppsUseCaseTest {
    private val repository: AppRepository = mockk()
    private val useCase = GetAppsUseCase(repository)

    @Test
    fun `invoke EXPECT returns flow from repository`() = runTest {
        val expectedApps = listOf(
            AppShort("1", "App1", "url1", Category.APP, "desc1"),
            AppShort("2", "App2", "url2", Category.GAME, "desc2")
        )
        val expectedFlow = flowOf(expectedApps)
        every { repository.getApps() } returns expectedFlow

        val result = useCase.invoke().first()

        assertEquals(expectedApps, result)
    }

    @Test
    fun `invoke when repository returns empty EXPECT returns empty flow`() = runTest {
        val emptyFlow = flowOf(emptyList<AppShort>())
        every { repository.getApps() } returns emptyFlow

        val result = useCase.invoke().first()

        assertEquals(emptyList<AppShort>(), result)
    }
}