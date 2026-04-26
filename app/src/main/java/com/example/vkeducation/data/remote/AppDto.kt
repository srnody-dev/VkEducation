package com.example.vkeducation.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AppDto(
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("developer")
    val developer: String? = null,

    @SerialName("category")
    val category: String,

    @SerialName("ageRating")
    val ageRating: Int? = null,

    @SerialName("size")
    val size: Float? = null,

    @SerialName("iconUrl")
    val iconUrl: String,

    @SerialName("screenshotUrlList")
    val screenshotUrlList: List<String>? = null,

    @SerialName("description")
    val description: String = "",
)