package com.example.spike

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.spike.data.User
import com.example.spike.preferences.UserPreference
import com.example.spike.ui.screen.RegisterScreen

class RegisterActivity : ComponentActivity() {

    private lateinit var userPrefs: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPrefs = UserPreference(this)

        setContent {
            RegisterScreen(
                onRegisterClick = { username, email, password, confirmPassword ->
                    if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                        Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                        return@RegisterScreen
                    }

                    if (!email.contains("@")) {
                        Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
                        return@RegisterScreen
                    }

                    if (password != confirmPassword) {
                        Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                        return@RegisterScreen
                    }

                    if (isUsernameTaken(username)) {
                        Toast.makeText(this, "Tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show()
                        return@RegisterScreen
                    }

                    val newUser = User(username, email, password)
                    userPrefs.saveUser(newUser)

                    Toast.makeText(this, "Đăng ký thành công. Vui lòng đăng nhập", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                },
                onLoginClick = {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                },
                isUsernameTaken = { username -> isUsernameTaken(username) }
            )
        }
    }

    private fun isUsernameTaken(username: String): Boolean {
        val users = userPrefs.getAllUsers()
        return users.any { it.username.equals(username, ignoreCase = true) }
    }
}
