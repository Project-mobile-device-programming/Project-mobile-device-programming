package com.example.spike.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.spike.preferences.UserPreference

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val userPref = remember { UserPreference(context) }
    val currentUser = remember { userPref.getCurrentUser() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (currentUser != null) {
            Text(text = "Xin chào, ${currentUser.username}", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Email: ${currentUser.email}", fontSize = 16.sp)
        } else {
            Text("Không có người dùng nào đang đăng nhập", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = {
            userPref.logout()
            navController.navigate("login") {
                popUpTo("home") { inclusive = true } // Xóa backstack tới màn hình home
            }
        }) {
            Text("Đăng xuất")
        }
    }
}
