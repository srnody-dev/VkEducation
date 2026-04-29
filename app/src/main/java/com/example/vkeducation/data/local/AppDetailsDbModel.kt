package com.example.vkeducation.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vkeducation.domain.entity.Category

@Entity(
    tableName = "appDetails"
)
data class AppDetailsDbModel(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val developer: String,
    val category: Category,
    val ageRating: Int,
    val size: Float,
    val iconUrl: String,
    val screenshotUrlList: List<String>,
    val description: String,
    val isInWishlist: Boolean = false
)
