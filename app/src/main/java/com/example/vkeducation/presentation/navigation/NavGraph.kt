package com.example.vkeducation.presentation.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vkeducation.presentation.screens.apps.AppsScreen
import com.example.vkeducation.presentation.screens.content.AppDetailsScreen


@Composable
fun NavGraph(
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Apps.route,
    ) {
        composable(Screen.Apps.route) {
            AppsScreen(
                onAppClick = {
                    navController.navigate(Screen.AppDetail.createRoute(it.id))
                },
                onNavigateToMenu = {

                }
            )
        }

        composable(
            route = Screen.AppDetail.route
        ) {
            val appId = Screen.AppDetail.getAppId(it.arguments)
            AppDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                appId = appId
            )
        }

    }
}


sealed class Screen(val route: String) {
    data object Apps : Screen("apps")
    data object AppDetail : Screen("apps/{id}") {
        fun createRoute(id: Int): String {
            return "apps/$id"
        }

        fun getAppId(arguments: Bundle?): Int {
            return arguments?.getString("id")?.toInt() ?: 0
        }

    }

}