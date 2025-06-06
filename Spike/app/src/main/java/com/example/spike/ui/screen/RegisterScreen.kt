package com.example.spike.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun RegisterScreen(
    onRegisterClick: (username: String, email: String, password: String, confirmPassword: String) -> Unit,
    onLoginClick: () -> Unit,
    isUsernameTaken: (String) -> Boolean = { false }
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val isPasswordValid = password.length >= 6
    val isConfirmPasswordValid = confirmPassword == password && confirmPassword.isNotEmpty()
    val isEmailValid = email.contains("@")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
    ) {
        // Nút quay lại góc trái trên
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Quay lại",
            modifier = Modifier
                .size(30.dp)
                .clickable { onLoginClick() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Đăng ký",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tài khoản
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                usernameError = isUsernameTaken(it)
            },
            label = { Text("Tài khoản") },
            isError = usernameError,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (username.isNotEmpty() && !usernameError) {
                    Text("✔", fontSize = 18.sp, color = Color(0xFF4CAF50))
                }
            }
        )
        if (usernameError) {
            Text(
                text = "Tên tài khoản đã tồn tại",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (email.isNotEmpty() && isEmailValid) {
                    Text("✔", fontSize = 18.sp, color = Color(0xFF4CAF50))
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mật khẩu
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu (ít nhất 6 ký tự)") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isPasswordValid) {
                        Text("✔", fontSize = 18.sp, color = Color(0xFF4CAF50))
                    }
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                        )
                    }
                }
            }
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Xác nhận mật khẩu") },
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isConfirmPasswordValid) {
                        Text("✔", fontSize = 18.sp, color = Color(0xFF4CAF50))
                    }
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (confirmPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    return@Button
                }
                if (usernameError) {
                    return@Button
                }
                if (!isEmailValid) {
                    return@Button
                }
                if (!isPasswordValid) {
                    return@Button
                }
                if (!isConfirmPasswordValid) {
                    return@Button
                }
                onRegisterClick(username, email, password, confirmPassword)
            },
            enabled = username.isNotBlank() && email.isNotBlank() && password.isNotBlank()
                    && confirmPassword.isNotBlank() && !usernameError && isEmailValid && isPasswordValid && isConfirmPasswordValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Đăng ký", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Bạn đã có tài khoản? ", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = "Đăng nhập",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onLoginClick() }
            )
        }
    }
}
