package com.example.vkeducation.data.mapper

import android.util.Log
import com.example.vkeducation.data.remote.AppDto
import com.example.vkeducation.domain.entity.App
import com.example.vkeducation.domain.entity.Category

fun AppDto.toDomain(): App {
    return App(
        id = id,
        name = name,
        developer = developer ?: "Unknown Developer",
        category = toCategory(category),
        ageRating = ageRating ?: 0,
        size = size ?: 0f,
        iconUrl = iconUrl,
        screenshotUrlList = screenshotUrlList ?: emptyList(),
        description = description
    )
}

fun List<AppDto>.toDomain(): List<App> {
    return map { it.toDomain() }
}

private fun toCategory(category: String): Category {
    return when (category) {
        "Приложения" -> Category.APP
        "Игры" -> Category.GAME
        "Производительность" -> Category.PRODUCTIVITY
        "Образ жизни" -> Category.SOCIAL
        "Образование" -> Category.EDUCATION
        "Развлечения" -> Category.ENTERTAINMENT
        "Музыка" -> Category.MUSIC
        "Видео" -> Category.VIDEO
        "Фото и видео" -> Category.PHOTOGRAPHY
        "Здоровье и фитнес" -> Category.HEALTH
        "Спорт" -> Category.SPORTS
        "Новости" -> Category.NEWS
        "Книги и справочники" -> Category.BOOKS
        "Бизнес" -> Category.BUSINESS
        "Финансы" -> Category.FINANCE
        "Путешествия" -> Category.TRAVEL
        "Навигация" -> Category.MAPS
        "Еда и напитки" -> Category.FOOD
        "Шопинг" -> Category.SHOPPING
        "Утилиты" -> Category.UTILITIES

        else -> {
            Log.d("Mapper", "Unknown category: '$category', using APP")
            Category.APP
        }
    }
}