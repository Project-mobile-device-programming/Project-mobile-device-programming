package com.example.spike

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.spike.preferences.UserPreference
import com.example.spike.ui.screen.ForgotPasswordScreen
import androidx.compose.runtime.*

class ForgotPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPrefs = UserPreference(this)

        setContent {
            var errorMessage by remember { mutableStateOf("") }

            ForgotPasswordScreen(
                errorMessage = errorMessage,
                onConfirm = { username ->
                    val user = userPrefs.getUser(username)
                    if (user != null) {
                        val intent = Intent(this, ResetPasswordActivity::class.java)
                        intent.putExtra("username", username)
                        startActivity(intent)
                        finish()
                    } else {
                        errorMessage = "Tài khoản không tồn tại"
                    }
                },
                onBackClick = {
                    finish()
                }
            )
        }
    }
}