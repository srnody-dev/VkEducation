package com.example.vkeducation.presentation.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            route = Screen.AppDetail.route,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            }
            )
        ) {
            AppDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

    }
}

sealed class Screen(val route: String) {
    data object Apps : Screen("appShorts")
    data object AppDetail : Screen("appShorts/{id}") {

        const val ARG_ID = "id"
        fun createRoute(id: String): String {
            return "appShorts/$id"
        }

        fun getAppId(arguments: Bundle?): String? {
            return arguments?.getString(ARG_ID)?.takeIf { it.isNotBlank() }
        }

    }

}