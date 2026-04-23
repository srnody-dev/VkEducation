package com.example.vkeducation.data.local

import com.example.vkeducation.data.remote.AppDto

interface LocalSource {
    fun getApps(): List<AppDto>
    fun getAppById(id: Int): AppDto?
}