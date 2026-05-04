package com.example.vkeducation.data.repository

import com.example.vkeducation.data.local.AppDetailsDbModel
import com.example.vkeducation.data.local.AppShortDbModel
import com.example.vkeducation.data.local.AppsDao
import com.example.vkeducation.data.remote.AppDto
import com.example.vkeducation.data.remote.AppsApiService
import com.example.vkeducation.domain.entity.AppDetails
import com.example.vkeducation.domain.entity.AppShort
import com.example.vkeducation.domain.entity.Category
import io.mockk.*
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


@ExperimentalCoroutinesApi
class AppRepositoryImplTest {

    private lateinit var repository: AppRepositoryImpl
    private lateinit var dao: AppsDao
    private lateinit var api: AppsApiService
    private val testDispatcher = StandardTestDispatcher()

    private val time = System.currentTimeMillis()

    @Before
    fun setup() {
        dao = mockk()
        api = mockk()
        repository = AppRepositoryImpl(dao, api)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    //getApps когда кэш свежий  ожидает возвращения кэшированных данных
    @Test
    fun `getApps when cache is fresh EXPECT returns cached data`() = runTest(testDispatcher) {
        val freshUpdateTime = time - (4 * 60 * 1000) //4 минуты назад
        val expectedApps = listOf(
            AppShort("1", "Test App", "url", Category.APP, "desc")
        )
        val cachedApps = listOf(
            AppShortDbModel("1", "Test App", "url", Category.APP, "desc",freshUpdateTime)
        )
        every { dao.getApp() } returns flowOf(cachedApps)
        coEvery { dao.getLastUpdateTime() } returns freshUpdateTime
        coEvery { dao.getAppsCount() } returns cachedApps.size

        val result = repository.getApps().first()

        assertEquals(expectedApps, result)
        coVerify(exactly = 0) { api.loadApps() }
    }

    //getApps когда кэш пуст ожидает загрузки из API
    @Test
    fun `getApps when cache is empty EXPECT loads from API`() = runTest(testDispatcher) {
        every { dao.getApp() } returns flowOf(emptyList())
        coEvery { dao.getAppsCount() } returns 0
        coEvery { api.loadApps() } returns emptyList()
        coEvery { dao.refreshApps(any()) } just Runs

        repository.getApps().first()

        coVerify(exactly = 1) { api.loadApps() }
    }

    //getApps когда кэш устарел ожидает загрузки из API
    @Test
    fun `getApps when cache is outdated EXPECT loads from API`() = runTest(testDispatcher) {
        val outdatedTime = time - (6 * 60 * 1000) // 6 минут назад
        val cachedApps = listOf(
            AppShortDbModel("1", "Old App", "url", Category.APP, "desc", outdatedTime)
        )

        every { dao.getApp() } returns flowOf(cachedApps)
        coEvery { dao.getLastUpdateTime() } returns outdatedTime
        coEvery { dao.getAppsCount() } returns cachedApps.size
        coEvery { api.loadApps() } returns emptyList()
        coEvery { dao.refreshApps(any()) } just Runs

        repository.getApps().first()

        coVerify(exactly = 1) { api.loadApps() }
    }

    //getApps при сбое API ожидает возврата пустого списка
    @Test
    fun `getApps when API fails EXPECT returns empty list`() = runTest(testDispatcher) {
        every { dao.getApp() } returns flowOf(emptyList())
        coEvery { dao.getAppsCount() } returns 0
        coEvery { api.loadApps() } throws RuntimeException("Network error")

        val result = repository.getApps().first()

        assertTrue(result.isEmpty())
    }

    //getAppDetailsById при кэшировании ожидает возвращение кэшированных данных
    @Test
    fun `getAppDetailsById when cached EXPECT returns cached data`() = runTest(testDispatcher) {
        val appId = "1"
        val expectedDetails = AppDetails(
            id = appId, name = "Cached App", developer = "Dev", category = Category.APP,
            ageRating = 12, size = 50f, iconUrl = "url", screenshotUrlList = emptyList(),
            description = "desc", isInWishlist = false
        )
        val cachedDetails = AppDetailsDbModel(
            id = appId, name = "Cached App", developer = "Dev", category = Category.APP,
            ageRating = 12, size = 50f, iconUrl = "url", screenshotUrlList = emptyList(),
            description = "desc", isInWishlist = false
        )

        coEvery { dao.getAppDetailsById(appId) } returns flowOf(cachedDetails)

        val result = repository.getAppDetailsById(appId)

        assertEquals(expectedDetails, result)
        coVerify(exactly = 0) { api.getAppById(any()) }
    }

    //getAppDetailsById когда он не кэширован ожидает загрузки из API
    @Test
    fun `getAppDetailsById when not cached EXPECT loads from API`() = runTest(testDispatcher) {
        val appId = "2"
        val apiDetails = AppDto(
            id = appId, name = "API App", iconUrl = "url", category = "Игры",
            description = "desc", developer = "API Dev", ageRating = 16,
            size = 100f, screenshotUrlList = emptyList()
        )

        coEvery { dao.getAppDetailsById(appId) } returns flowOf(null)
        coEvery { api.getAppById(appId) } returns apiDetails
        coEvery { dao.addAppDetails(any()) } just Runs

        repository.getAppDetailsById(appId)

        coVerify(exactly = 1) { api.getAppById(appId) }
    }

    //getAppDetailsById если он не кэширован ожидает возврата загруженных данных
    @Test
    fun `getAppDetailsById when not cached EXPECT returns loaded data`() = runTest(testDispatcher) {
        val appId = "2"
        val expectedDetails = AppDetails(
            id = appId, name = "API App", developer = "API Dev", category = Category.GAME,
            ageRating = 16, size = 100f, iconUrl = "url", screenshotUrlList = emptyList(),
            description = "desc", isInWishlist = false
        )
        val apiDetails = AppDto(
            id = appId, name = "API App", iconUrl = "url", category = "Игры",
            description = "desc", developer = "API Dev", ageRating = 16,
            size = 100f, screenshotUrlList = emptyList()
        )

        coEvery { dao.getAppDetailsById(appId) } returns flowOf(null)
        coEvery { api.getAppById(appId) } returns apiDetails
        coEvery { dao.addAppDetails(any()) } just Runs
        val result = repository.getAppDetailsById(appId)

        assertEquals(expectedDetails, result)
    }

    //getAppDetailsById при сбое API ожидает возвращение значение null
    @Test
    fun `getAppDetailsById when API fails EXPECT returns null`() = runTest(testDispatcher) {
        val appId = "3"

        coEvery { dao.getAppDetailsById(appId) } returns flowOf(null)
        coEvery { api.getAppById(appId) } throws RuntimeException("API Error")

        val result = repository.getAppDetailsById(appId)

        assertNull(result)
    }

    //refreshApps ожидает загрузку из API
    @Test
    fun `refreshApps EXPECT loads from API`() = runTest(testDispatcher) {
        coEvery { api.loadApps() } returns emptyList()
        coEvery { dao.refreshApps(any()) } just Runs

        repository.refreshApps()

        coVerify(exactly = 1) { api.loadApps() }
    }

    //refreshApps ожидает сохранение данных в кэш
    @Test
    fun `refreshApps EXPECT saves data to cache`() = runTest(testDispatcher) {
        val apiApps = listOf(
            AppDto(
                "1",
                "App",
                "Developer",
                "Category",
                5,
                50f,
                "url",
                listOf("scr1"),
                "description"
            )
        )
        coEvery { api.loadApps() } returns apiApps
        coEvery { dao.refreshApps(any()) } just Runs

        repository.refreshApps()

        coVerify(exactly = 1) { dao.refreshApps(any()) }
    }


    //toggleWishlist ожидает вызова dao для переключения статуса
    @Test
    fun `toggleWishlist EXPECT calls dao to toggle status`() = runTest(testDispatcher) {
        val appId = "wishlist_1"
        coEvery { dao.toggleWishlistStatus(appId) } just Runs

        repository.toggleWishlist(appId)

        coVerify(exactly = 1) { dao.toggleWishlistStatus(appId) }
    }
}