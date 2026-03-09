package com.example.vkeducation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.vkeducation.presentation.screens.second.SecondScreen
import com.example.vkeducation.ui.theme.VkEducationTheme

class SecondActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text = intent.getStringExtra("text") ?: ""

        setContent {
            VkEducationTheme {
                SecondScreen(text = text)
            }
        }
    }

}