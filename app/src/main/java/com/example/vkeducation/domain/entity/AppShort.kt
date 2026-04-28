package com.example.vkeducation.domain.entity

data class AppShort(
    val id: String,
    val name: String,
    val iconUrl: String,
    val category: Category,
    val description: String,
)