package com.example.spike

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.spike.preferences.UserPreference
import com.example.spike.ui.screen.ResetPasswordScreen

class ResetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPrefs = UserPreference(this)
        val username = intent.getStringExtra("username") ?: ""

        setContent {
            ResetPasswordScreen(
                onPasswordReset = { newPassword ->
                    userPrefs.updatePassword(username, newPassword)
                    Toast.makeText(this, "Đặt lại mật khẩu thành công", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                },
                onBackClick = {
                    val intent = Intent(this, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            )
        }
    }
}