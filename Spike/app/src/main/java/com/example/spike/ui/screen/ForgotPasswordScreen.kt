package com.example.spike.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ForgotPasswordScreen(
    errorMessage: String? = null,
    onConfirm: (String) -> Unit,
    onBackClick: () -> Unit,
    onErrorDismiss: () -> Unit = {}
) {
    var username by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Quay lại")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("Quên mật khẩu", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                if (errorMessage != null) onErrorDismiss() // reset lỗi khi người dùng nhập lại
            },
            label = { Text("Tên đăng nhập") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onConfirm(username)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Xác nhận")
        }
    }
}
