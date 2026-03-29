package com.example.vkeducation.presentation

import com.example.vkeducation.R

object AppIconMapper {
    fun mapToIconResId(iconKey: String): Int {
        return when (iconKey) {
            "sber_icon" -> R.drawable.store
            "yandex_icon" -> R.drawable.tv
            "mail_icon" -> R.drawable.music
            "mts_icon" -> R.drawable.intel
            else -> R.drawable.phone
        }
    }
}