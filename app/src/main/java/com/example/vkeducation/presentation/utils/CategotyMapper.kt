package com.example.vkeducation.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.vkeducation.R
import com.example.vkeducation.domain.entity.Category

@Composable
fun Category.toCategoryText(): String = when (this) {
    Category.APP -> stringResource(R.string.app)
    Category.GAME -> stringResource(R.string.game)
    Category.PRODUCTIVITY -> stringResource(R.string.productivity)
    Category.SOCIAL -> stringResource(R.string.social)
    Category.EDUCATION -> stringResource(R.string.education)
    Category.ENTERTAINMENT -> stringResource(R.string.entertaiment)
    Category.MUSIC -> stringResource(R.string.music)
    Category.VIDEO -> stringResource(R.string.video)
    Category.PHOTOGRAPHY -> stringResource(R.string.photo)
    Category.HEALTH -> stringResource(R.string.health)
    Category.SPORTS -> stringResource(R.string.sports)
    Category.NEWS -> stringResource(R.string.news)
    Category.BOOKS -> stringResource(R.string.books)
    Category.BUSINESS -> stringResource(R.string.business)
    Category.FINANCE -> stringResource(R.string.finance)
    Category.TRAVEL -> stringResource(R.string.travel)
    Category.MAPS -> stringResource(R.string.maps)
    Category.FOOD -> stringResource(R.string.food)
    Category.SHOPPING -> stringResource(R.string.shopping)
    Category.UTILITIES -> stringResource(R.string.utils)
}
