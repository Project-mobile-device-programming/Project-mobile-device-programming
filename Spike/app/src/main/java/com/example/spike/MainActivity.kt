package com.example.spike

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.spike.preferences.UserPreference
import com.example.spike.ui.components.BottomNavigationBar
import com.example.spike.ui.screen.*
import com.example.spike.ui.theme.SpikeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SpikeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val userPref = remember { UserPreference(applicationContext) }

                    // Đọc trạng thái đăng nhập mỗi lần hiển thị
                    val isLoggedIn = userPref.getCurrentUser() != null

                    var loginError by remember { mutableStateOf<String?>(null) }

                    val startDestination = if (isLoggedIn) "home" else "login"

                    Scaffold(
                        bottomBar = {
                            // Chỉ hiện bottom bar khi đã đăng nhập
                            if (isLoggedIn) {
                                BottomNavigationBar(navController)
                            }
                        }
                    ) { paddingValues ->

                        NavHost(
                            navController = navController,
                            startDestination = startDestination,
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            // Đăng nhập
                            composable("login") {
                                LoginScreen(
                                    onLoginClick = { username, password ->
                                        val user = userPref.getUser(username)
                                        if (user != null && user.password == password) {
                                            userPref.setCurrentUser(username)
                                            navController.navigate("home") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        } else {
                                            loginError = "Sai tài khoản hoặc mật khẩu"
                                        }
                                    },
                                    onRegisterClick = {
                                        navController.navigate("register")
                                    },
                                    onForgotPasswordClick = {
                                        navController.navigate("forgot_password")
                                    },
                                    onFacebookLoginClick = { /* TODO */ },
                                    onGoogleLoginClick = { /* TODO */ },
                                    errorMessage = loginError,
                                    onErrorDismiss = { loginError = null }
                                )
                            }

                            // Đăng ký
                            composable("register") {
                                RegisterScreen(
                                    onRegisterClick = { username, email, password, confirmPassword ->
                                        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) return@RegisterScreen
                                        if (!email.contains("@") || password.length < 6 || password != confirmPassword) return@RegisterScreen
                                        if (userPref.getAllUsers().any { it.username.equals(username, true) }) return@RegisterScreen

                                        userPref.saveUser(com.example.spike.data.User(username, email, password))
                                        userPref.setCurrentUser(username)

                                        navController.navigate("home") {
                                            popUpTo("register") { inclusive = true }
                                        }
                                    },
                                    onLoginClick = {
                                        navController.popBackStack()
                                    },
                                    isUsernameTaken = { username ->
                                        userPref.getAllUsers().any { it.username.equals(username, true) }
                                    }
                                )
                            }

                            // Màn hình quên mật khẩu
                            composable("forgot_password") {
                                var errorMessage by remember { mutableStateOf<String?>(null) }

                                ForgotPasswordScreen(
                                    errorMessage = errorMessage,
                                    onConfirm = { username ->
                                        val user = userPref.getUser(username)
                                        if (user != null) {
                                            navController.navigate("reset_password/$username")
                                        } else {
                                            errorMessage = "Tài khoản không tồn tại"
                                        }
                                    },
                                    onBackClick = {
                                        navController.popBackStack()
                                    },
                                    onErrorDismiss = { errorMessage = null }
                                )
                            }

                            // Đặt lại mật khẩu
                            composable("reset_password/{username}") { backStackEntry ->
                                val username = backStackEntry.arguments?.getString("username") ?: ""
                                ResetPasswordScreen(
                                    onPasswordReset = { newPassword ->
                                        userPref.updatePassword(username, newPassword)
                                        Toast.makeText(this@MainActivity, "Đặt lại mật khẩu thành công", Toast.LENGTH_SHORT).show()
                                        navController.navigate("login") {
                                            popUpTo("reset_password/$username") { inclusive = true }
                                        }
                                    },
                                    onBackClick = {
                                        navController.popBackStack()
                                    }
                                )
                            }

                            // Các màn hình chính
                            composable("home") {
                                if (isLoggedIn) HomeScreen(navController)
                                else navController.navigate("login")
                            }
                            composable("search") {
                                if (isLoggedIn) SearchScreen()
                                else navController.navigate("login")
                            }
                            composable("library") {
                                if (isLoggedIn) LibraryScreen()
                                else navController.navigate("login")
                            }
                            composable("settings") {
                                if (isLoggedIn) SettingsScreen(navController)
                                else navController.navigate("login")
                            }
                        }
                    }
                }
            }
        }
    }
}
