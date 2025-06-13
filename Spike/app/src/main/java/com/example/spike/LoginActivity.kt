package com.example.spike

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.spike.preferences.UserPreference
import com.example.spike.ui.screen.LoginScreen

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPrefs = UserPreference(this)

        setContent {
            var errorMessage by remember { mutableStateOf<String?>(null) }

            LoginScreen(
                onLoginClick = { username, password ->
                    val user = userPrefs.getUser(username)
                    if (user == null || user.password != password) {
                        errorMessage = "Sai tài khoản hoặc mật khẩu"
                    } else {
                        errorMessage = null

                        // Lưu trạng thái đăng nhập và username
                        userPrefs.setCurrentUser(username) // Lưu username vào current_user
                        val sharedPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        sharedPrefs.edit().putBoolean("is_logged_in", true).apply()

                        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                },
                onRegisterClick = {
                    startActivity(Intent(this, RegisterActivity::class.java))
                    finish()
                },
                onForgotPasswordClick = {
                    startActivity(Intent(this, ForgotPasswordActivity::class.java))
                },
                onFacebookLoginClick = {},
                onGoogleLoginClick = {},
                errorMessage = errorMessage,
                onErrorDismiss = { errorMessage = null }
            )
        }
    }
}