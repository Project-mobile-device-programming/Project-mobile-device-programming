package com.example.spike.preferences

import android.content.Context
import com.example.spike.data.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserPreference(context: Context) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Hàm lưu hoặc cập nhật user
    fun saveUser(user: User) {
        val users = getAllUsers().toMutableList()
        // Kiểm tra user có tồn tại chưa, nếu có thì cập nhật, không thì thêm mới
        val index = users.indexOfFirst { it.username.equals(user.username, ignoreCase = true) }
        if (index >= 0) {
            users[index] = user // cập nhật user cũ
        } else {
            users.add(user) // thêm user mới
        }
        val json = gson.toJson(users)
        prefs.edit().putString("users", json).apply()
    }

    fun getAllUsers(): List<User> {
        val json = prefs.getString("users", "[]")
        val type = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(json, type)
    }

    fun getUser(username: String): User? {
        val users = getAllUsers()
        return users.find { it.username.equals(username, ignoreCase = true) }
    }

    fun updatePassword(username: String, newPassword: String) {
        val user = getUser(username)
        if (user != null) {
            val updatedUser = user.copy(password = newPassword)
            saveUser(updatedUser)  // saveUser đã xử lý cập nhật rồi
        }
    }

    fun setCurrentUser(username: String) {
        prefs.edit().putString("current_user", username).apply()
    }

    fun getCurrentUser(): User? {
        val username = prefs.getString("current_user", null)
        return username?.let { getUser(it) }
    }

    fun logout() {
        prefs.edit().remove("current_user").apply()
    }
}
