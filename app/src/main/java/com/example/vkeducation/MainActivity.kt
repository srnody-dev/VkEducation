package com.example.vkeducation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import com.example.vkeducation.presentation.navigation.NavGraph
import com.example.vkeducation.presentation.screens.apps.AppsScreen
import com.example.vkeducation.ui.theme.VkEducationTheme
import com.example.vkeducation.presentation.screens.content.AppDetailsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VkEducationTheme {
                NavGraph()
            }
        }
    }

}
