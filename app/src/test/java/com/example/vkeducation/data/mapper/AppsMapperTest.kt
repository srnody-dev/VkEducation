package com.example.vkeducation.data.mapper

import com.example.vkeducation.data.local.AppDetailsDbModel
import com.example.vkeducation.data.local.AppShortDbModel
import com.example.vkeducation.data.remote.AppDto
import com.example.vkeducation.domain.entity.Category
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

class AppsMapperTest {
    private fun createTestAppDto(
        id: String = "1",
        name: String = "Test App",
        iconUrl: String = "iconUrl",
        category: String = "Игры",
        description: String = "Test description",
        developer: String? = "Test Developer",
        ageRating: Int? = 12,
        size: Float? = 50f,
        screenshotUrlList: List<String>? = listOf("scr1.png")
    ) = AppDto(
        id = id,
        name = name,
        iconUrl = iconUrl,
        category = category,
        description = description,
        developer = developer,
        ageRating = ageRating,
        size = size,
        screenshotUrlList = screenshotUrlList
    )

    private fun createTestAppDetailsDbModel(
        id: String = "1",
        name: String = "Test App",
        developer: String = "Test Developer",
        category: Category = Category.APP,
        ageRating: Int = 12,
        size: Float = 50f,
        iconUrl: String = "url",
        screenshotUrlList: List<String> = listOf("scr1.png"),
        description: String = "desc",
        isInWishlist: Boolean = false
    ) = AppDetailsDbModel(
        id = id,
        name = name,
        developer = developer,
        category = category,
        ageRating = ageRating,
        size = size,
        iconUrl = iconUrl,
        screenshotUrlList = screenshotUrlList,
        description = description,
        isInWishlist = isInWishlist
    )

    @Test
    fun `toAppShort when valid DTO EXPECT maps correctly`() {
        val dto = createTestAppDto(
            id = "1",
            name = "Test App",
            iconUrl = "iconUrl",
            category = "Игры",
            description = "Test description"
        )

        val result = dto.toAppShort()

        assertEquals("1", result.id)
        assertEquals("Test App", result.name)
        assertEquals("iconUrl", result.iconUrl)
        assertEquals(Category.GAME, result.category)
        assertEquals("Test description", result.description)
    }

    @Test
    fun `toAppShort when unknown category EXPECT maps to APP`() {
        val dto = createTestAppDto(
            category = "Unknown Category"
        )

        val result = dto.toAppShort()

        assertEquals(Category.APP, result.category)
    }


    @Test
    fun `toAppDetails when all fields present EXPECT maps correctly`() {
        val dto = createTestAppDto(
            id = "1",
            name = "Test App",
            iconUrl = "iconUrl",
            category = "Образование",
            description = "Detailed description",
            developer = "Test Developer",
            ageRating = 18,
            size = 120.5f,
            screenshotUrlList = listOf("scr1.png", "scr2.png")
        )

        val result = dto.toAppDetails()

        assertEquals("1", result.id)
        assertEquals("Test App", result.name)
        assertEquals("Test Developer", result.developer)
        assertEquals(Category.EDUCATION, result.category)
        assertEquals(18, result.ageRating)
        assertEquals(120.5f, result.size, 0.01f)
        assertEquals(listOf("scr1.png", "scr2.png"), result.screenshotUrlList)
        assertFalse(result.isInWishlist)
    }

    @Test
    fun `toAppDetails when null fields EXPECT uses default values`() {
        val dto = createTestAppDto(
            developer = null,
            ageRating = null,
            size = null,
            screenshotUrlList = null
        )

        val result = dto.toAppDetails()

        assertEquals("Unknown", result.developer)
        assertEquals(0, result.ageRating)
        assertEquals(0f, result.size, 0.01f)
        assertTrue(result.screenshotUrlList.isEmpty())
    }


    @Test
    fun `toAppShortDb EXPECT maps to AppShortDbModel correctly`() {
        val dto = createTestAppDto(
            id = "1",
            name = "DB App",
            iconUrl = "iconUrl",
            category = "Музыка",
            description = "DB description"
        )

        val result = dto.toAppShortDb()

        assertEquals("1", result.id)
        assertEquals("DB App", result.name)
        assertEquals(Category.MUSIC, result.category)
        assertEquals("DB description", result.description)
    }


    @Test
    fun `toAppDetailsDb EXPECT maps to AppDetailsDbModel correctly`() {
        val dto = createTestAppDto(
            id = "1",
            name = "Details DB App",
            iconUrl = "url",
            category = "Видео",
            description = "Video app",
            developer = "Video Dev",
            ageRating = 16,
            size = 200f,
            screenshotUrlList = listOf("scr1.png")
        )

        val result = dto.toAppDetailsDb()

        assertEquals("1", result.id)
        assertEquals("Details DB App", result.name)
        assertEquals("Video Dev", result.developer)
        assertEquals(Category.VIDEO, result.category)
        assertEquals(16, result.ageRating)
        assertEquals(200f, result.size, 0.01f)
        assertEquals(listOf("scr1.png"), result.screenshotUrlList)
        assertFalse(result.isInWishlist)
    }


    @Test
    fun `toAppDomain when valid DbModel EXPECT maps to AppShort correctly`() {
        val dbModel = AppShortDbModel(
            id = "1",
            name = "Domain App",
            iconUrl = "https://test.com/icon.png",
            category = Category.SHOPPING,
            description = "Shopping app"
        )

        val result = dbModel.toAppDomain()

        assertEquals("1", result.id)
        assertEquals("Domain App", result.name)
        assertEquals(Category.SHOPPING, result.category)
        assertEquals("Shopping app", result.description)
    }


    @Test
    fun `toAppDetailsDomain when isInWishlist true EXPECT preserves wishlist status`() {
        val dbModel = createTestAppDetailsDbModel(
            isInWishlist = true
        )

        val result = dbModel.toAppDetailsDomain()

        assertTrue(result.isInWishlist)
    }

    @Test
    fun `toAppDetailsDomain when screenshotUrlList is empty EXPECT returns empty list`() {
        val dbModel = createTestAppDetailsDbModel(
            screenshotUrlList = emptyList()
        )

        val result = dbModel.toAppDetailsDomain()

        assertTrue(result.screenshotUrlList.isEmpty())
    }

    @Test
    fun `toAppDetailsDomain when all fields have values EXPECT preserves all values`() {
        val dbModel = createTestAppDetailsDbModel(
            id = "1",
            name = "Test App",
            developer = "Test Developer",
            category = Category.GAME,
            ageRating = 16,
            size = 100f,
            iconUrl = "url",
            screenshotUrlList = listOf("scr1.png", "scr2.png"),
            description = "desc",
            isInWishlist = true
        )

        val result = dbModel.toAppDetailsDomain()

        assertEquals("Test Developer", result.developer)
        assertEquals(16, result.ageRating)
        assertEquals(100f, result.size, 0.01f)
        assertEquals(listOf("scr1.png", "scr2.png"), result.screenshotUrlList)
        assertTrue(result.isInWishlist)
    }


    @ParameterizedTest
    @CsvSource(
        "Приложения, APP",
        "Игры, GAME",
        "Производительность, PRODUCTIVITY",
        "Образ жизни, SOCIAL",
        "Образование, EDUCATION",
        "Развлечения, ENTERTAINMENT",
        "Музыка, MUSIC",
        "Видео, VIDEO",
        "Фото и видео, PHOTOGRAPHY",
        "Здоровье и фитнес, HEALTH",
        "Спорт, SPORTS",
        "Новости, NEWS",
        "Книги и справочники, BOOKS",
        "Бизнес, BUSINESS",
        "Финансы, FINANCE",
        "Путешествия, TRAVEL",
        "Навигация, MAPS",
        "Еда и напитки, FOOD",
        "Шопинг, SHOPPING",
        "Утилиты, UTILITIES"
    )
    fun `toCategory when known category EXPECT maps correctly`(input: String, expected: String) {
        val dto = createTestAppDto(category = input)
        val result = dto.toAppShort()
        assertEquals(Category.valueOf(expected), result.category)
    }
}