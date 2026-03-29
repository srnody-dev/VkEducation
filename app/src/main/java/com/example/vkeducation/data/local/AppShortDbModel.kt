package com.example.vkeducation.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vkeducation.domain.entity.Category


@Entity(
    tableName = "appShorts"
)
data class AppShortDbModel(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val iconUrl: String,
    val category: Category,
    val description: String,
    val lastUpdated: Long = System.currentTimeMillis()
)