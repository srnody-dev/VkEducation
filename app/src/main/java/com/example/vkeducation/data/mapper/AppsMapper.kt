package com.example.vkeducation.data.mapper

import android.util.Log
import com.example.vkeducation.data.remote.AppDto
import com.example.vkeducation.domain.entity.App
import com.example.vkeducation.domain.entity.Category

fun AppDto.toDomain(): App {
    return try {
        App(
            id = id,
            name = name,
            developer = developer,
            category = toCategory(category),
            ageRating = ageRating,
            size = size,
            iconUrl = iconUrl,
            screenshotUrlList = screenshotUrlList,
            description = description
        )
    } catch (e: Exception) {
        Log.e("Mapper", "Error converting to DB model: ${e.message}")
         App(
             id = -1,
             name = "",
             developer = "",
             category = Category.APP,
             ageRating = 0,
             size = 0f,
             iconUrl = "",
             screenshotUrlList = emptyList(),
             description = ""
         )
    }
}

fun List<AppDto>.toDomain(): List<App> {
    return map { it.toDomain() }
}

private fun toCategory(category: String): Category {
    return when (category.uppercase()) {
        "APP" -> Category.APP
        "GAME" -> Category.GAME
        "FINANCE" -> Category.FINANCE
        "INSTRUMENTS" -> Category.INSTRUMENTS
        "TRANSPORT" -> Category.TRANSPORT
        else -> Category.APP
    }
}