package com.example.spike.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.spike.data.User
import com.example.spike.preferences.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userPref = UserPreference(application)

    fun registerUser(
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            onError("Vui lòng nhập đầy đủ thông tin")
            return
        }
        if (password != confirmPassword) {
            onError("Mật khẩu xác nhận không khớp")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val existingUser = userPref.getUser(username)
            if (existingUser != null) {
                onError("Tài khoản đã tồn tại")
            } else {
                val newUser = User(username, email, password)
                userPref.saveUser(newUser)
                onSuccess()
            }
        }
    }

    fun loginUser(
        username: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (username.isBlank() || password.isBlank()) {
            onError("Vui lòng nhập đầy đủ thông tin")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val user = userPref.getUser(username)
            if (user != null && user.password == password) {
                onSuccess()
            } else {
                onError("Sai tài khoản hoặc mật khẩu")
            }
        }
    }

    // Thêm hàm resetPassword
    fun resetPassword(
        username: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (username.isBlank() || newPassword.isBlank()) {
            onError("Thông tin không hợp lệ")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val user = userPref.getUser(username)
            if (user == null) {
                onError("Người dùng không tồn tại")
            } else {
                val updatedUser = user.copy(password = newPassword)
                userPref.saveUser(updatedUser) // Cập nhật mật khẩu mới
                onSuccess()
            }
        }
    }
}
