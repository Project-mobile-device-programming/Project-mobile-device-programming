package com.example.spike.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color

@Composable
fun LibraryScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(16.dp)) {
        Text("Thư viện của bạn", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Bạn chưa có bài hát nào được lưu.")
    }
}
