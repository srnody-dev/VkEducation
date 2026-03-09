package com.example.vkeducation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.vkeducation.presentation.screens.first.FirstScreen
import com.example.vkeducation.ui.theme.VkEducationTheme
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VkEducationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val textState = remember { mutableStateOf("") }
                    FirstScreen(
                        modifier = Modifier.padding(innerPadding),
                        onTextChange = { textState.value = it },
                        onOpenClick = {

                            val text = textState.value.trim()
                            if (!validateText(text)) return@FirstScreen
                            val intent = Intent(this, SecondActivity::class.java).apply {
                                putExtra("text", text)
                            }
                            startActivity(intent)
                        },
                        onCallClick = {
                            val phone = textState.value.trim()
                            if (!validateText(phone)) return@FirstScreen
                            if (!phone.all { it.isDigit() }) {
                                Toast.makeText(this,
                                    getString(R.string.enter_corrected_number), Toast.LENGTH_SHORT).show()
                                return@FirstScreen
                            }
                            val intent = Intent(
                                Intent.ACTION_DIAL,
                                "tel:$phone".toUri()
                            )
                            startActivity(intent)
                        },
                        onShareClick = {
                            val text = textState.value.trim()
                            if (!validateText(text)) return@FirstScreen
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type="text/plain"
                                putExtra(Intent.EXTRA_TEXT, text)
                            }
                            startActivity(intent)
                        },
                        text = textState.value
                    )
                }
            }
        }
    }
    fun validateText(text: String): Boolean {
        if (text.isBlank()) {
            Toast.makeText(this, getString(R.string.enter_ext), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
