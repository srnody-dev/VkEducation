package com.example.vkeducation.data.mapper

import android.util.Log
import com.example.vkeducation.data.local.AppDetailsDbModel
import com.example.vkeducation.data.local.AppShortDbModel
import com.example.vkeducation.data.remote.AppDto
import com.example.vkeducation.domain.entity.AppDetails
import com.example.vkeducation.domain.entity.AppShort
import com.example.vkeducation.domain.entity.Category

fun AppDto.toAppShort(): AppShort {
    return AppShort(
        id = id,
        name = name,
        iconUrl = iconUrl,
        category = toCategory(category),
        description = description
    )
}

fun AppDto.toAppDetails(): AppDetails {
    return AppDetails(
        id = id,
        name = name,
        developer = developer ?: "Unknown",
        category = toCategory(category),
        ageRating = ageRating ?: 0,
        size = size ?: 0f,
        iconUrl = iconUrl,
        screenshotUrlList = screenshotUrlList ?: emptyList(),
        description = description,
        isInWishlist = false
    )
}

fun AppDto.toAppShortDb(): AppShortDbModel {
    return AppShortDbModel(
        id = id,
        name = name,
        iconUrl = iconUrl,
        category = toCategory(category),
        description = description
    )
}

fun AppDto.toAppDetailsDb(): AppDetailsDbModel {
    return AppDetailsDbModel(
        id = id,
        name = name,
        developer = developer ?: "Unknown",
        category = toCategory(category),
        ageRating = ageRating ?: 0,
        size = size ?: 0f,
        iconUrl = iconUrl,
        screenshotUrlList = screenshotUrlList ?: emptyList(),
        description = description,
        isInWishlist = false
    )
}

fun AppShortDbModel.toAppDomain(): AppShort {
    return AppShort(
        id = id,
        name = name,
        iconUrl = iconUrl,
        category = category,
        description = description
    )
}

fun AppDetailsDbModel.toAppDetailsDomain(): AppDetails {
    return AppDetails(
        id = id,
        name = name,
        developer = developer ?: "Unknown",
        category = category,
        ageRating = ageRating ?: 0,
        size = size ?: 0f,
        iconUrl = iconUrl,
        screenshotUrlList = screenshotUrlList ?: emptyList(),
        description = description,
        isInWishlist = isInWishlist
    )
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