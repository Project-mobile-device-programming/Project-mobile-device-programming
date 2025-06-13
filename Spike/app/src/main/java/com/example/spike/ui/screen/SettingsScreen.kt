package com.example.spike.ui.screen

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.spike.LoginActivity
import com.example.spike.data.User
import com.example.spike.preferences.UserPreference
import kotlin.random.Random

/**
 * SettingsScreen displays the user profile and settings options.
 * It shows an avatar, user details, and provides actions like logout and settings.
 */
@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val userPref = remember { UserPreference(context) }
    val currentUser = userPref.getCurrentUser()

    // Log for debugging
    Log.d("SettingsScreen", "CurrentUser: $currentUser")

    // State for dialogs
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var notificationEnabled by remember { mutableStateOf(true) } // Placeholder for notification
    var showPrivacyDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // User profile section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    if (currentUser != null) {
                        UserProfileSection(
                            username = currentUser.username,
                            email = currentUser.email
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        SettingsOptionsSection(
                            onEditProfileClick = { showEditProfileDialog = true },
                            onChangePasswordClick = { showChangePasswordDialog = true },
                            onNotificationSettingsClick = { notificationEnabled = !notificationEnabled },
                            onPrivacySettingsClick = { showPrivacyDialog = true }
                        )
                    } else {
                        NoUserSection(
                            onLoginClick = {
                                val intent = Intent(context, LoginActivity::class.java)
                                context.startActivity(intent)
                                (context as? Activity)?.finish()
                            }
                        )
                    }
                }

                // Logout button (only shown when logged in)
                if (currentUser != null) {
                    LogoutButton(
                        onLogoutClick = {
                            userPref.logout()
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                            (context as? Activity)?.finish()
                        }
                    )
                }
            }
        }
    )

    // Edit Profile Dialog
    if (showEditProfileDialog && currentUser != null) {
        EditProfileDialog(
            currentUser = currentUser,
            userPref = userPref,
            onDismiss = { showEditProfileDialog = false },
            onSave = { updatedUser ->
                userPref.saveUser(updatedUser)
                showEditProfileDialog = false
            }
        )
    }

    // Change Password Dialog
    if (showChangePasswordDialog && currentUser != null) {
        ChangePasswordDialog(
            userPref = userPref,
            currentUser = currentUser,
            onDismiss = { showChangePasswordDialog = false },
            onSave = { newPassword ->
                userPref.updatePassword(currentUser.username, newPassword)
                showChangePasswordDialog = false
            }
        )
    }

    // Privacy Settings Dialog (Placeholder)
    if (showPrivacyDialog) {
        PrivacySettingsDialog(
            onDismiss = { showPrivacyDialog = false },
            onDeleteAccount = {
                // TODO: Implement account deletion
                showPrivacyDialog = false
            }
        )
    }
}

/**
 * Displays the user profile with an avatar, username, and email.
 */
@Composable
private fun UserProfileSection(username: String, email: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        UserAvatar(username = username)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Xin chào, $username",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = email,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}

/**
 * Displays the user avatar with the first letter of the username and a random background color.
 */
@Composable
private fun UserAvatar(username: String) {
    val initial = username.firstOrNull()?.uppercase() ?: "?"
    val hash = username.hashCode()
    val random = Random(hash)
    val randomColor = Color(
        red = random.nextFloat(),
        green = random.nextFloat(),
        blue = random.nextFloat(),
        alpha = 1f
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(randomColor)
            .clickable { /* TODO: Allow avatar editing in future */ },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 40.sp,
                fontWeight = FontWeight.SemiBold
            ),
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Displays settings options as a list of clickable items.
 */
@Composable
private fun SettingsOptionsSection(
    onEditProfileClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onNotificationSettingsClick: () -> Unit,
    onPrivacySettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SettingsOptionItem(
            title = "Chỉnh sửa hồ sơ",
            onClick = onEditProfileClick
        )
        SettingsOptionItem(
            title = "Thay đổi mật khẩu",
            onClick = onChangePasswordClick
        )
        SettingsOptionItem(
            title = "Cài đặt thông báo",
            onClick = onNotificationSettingsClick
        )
        SettingsOptionItem(
            title = "Cài đặt quyền riêng tư",
            onClick = onPrivacySettingsClick
        )
    }
}

/**
 * Individual settings option item with a clickable surface.
 */
@Composable
private fun SettingsOptionItem(title: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(16.dp)
        )
    }
}

/**
 * Displays a message and login button when no user is logged in.
 */
@Composable
private fun NoUserSection(onLoginClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxHeight()
    ) {
        Text(
            text = "Không có người dùng nào đang đăng nhập",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Đi đến Đăng nhập")
        }
    }
}

/**
 * Logout button at the bottom of the screen.
 */
@Composable
private fun LogoutButton(onLogoutClick: () -> Unit) {
    Button(
        onClick = onLogoutClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = "Đăng xuất",
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp)
        )
    }
}

/**
 * Dialog for editing user profile (username and email).
 */
@Composable
private fun EditProfileDialog(
    currentUser: User,
    userPref: UserPreference,
    onDismiss: () -> Unit,
    onSave: (User) -> Unit
) {
    var username by remember { mutableStateOf(currentUser.username) }
    var email by remember { mutableStateOf(currentUser.email) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Chỉnh sửa hồ sơ",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Tên tài khoản") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null
                )
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            when {
                                username.isBlank() || email.isBlank() -> {
                                    errorMessage = "Vui lòng điền đầy đủ thông tin"
                                }
                                !email.contains("@") -> {
                                    errorMessage = "Email không hợp lệ"
                                }
                                userPref.getAllUsers().any { it.username == username && it.username != currentUser.username } -> {
                                    errorMessage = "Tên tài khoản đã tồn tại"
                                }
                                else -> {
                                    errorMessage = null
                                    val updatedUser = currentUser.copy(username = username, email = email)
                                    onSave(updatedUser)
                                }
                            }
                        }
                    ) {
                        Text("Lưu")
                    }
                }
            }
        }
    }
}

/**
 * Dialog for changing user password with real-time validation for all fields.
 */
@Composable
private fun ChangePasswordDialog(
    userPref: UserPreference,
    currentUser: User,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Check if old password is correct, only when not empty
    val isOldPasswordCorrect = if (oldPassword.isNotBlank()) {
        oldPassword == currentUser.password
    } else {
        null // No validation when empty
    }

    // Check if new password is valid (not empty and at least 6 characters)
    val isNewPasswordValid = if (newPassword.isNotBlank()) {
        newPassword.length >= 6
    } else {
        null // No validation when empty
    }

    // Check if confirm password matches new password, only when not empty
    val isConfirmPasswordValid = if (confirmPassword.isNotBlank()) {
        confirmPassword == newPassword
    } else {
        null // No validation when empty
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Thay đổi mật khẩu",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = { Text("Mật khẩu cũ") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = errorMessage != null,
                    trailingIcon = {
                        isOldPasswordCorrect?.let { correct ->
                            Text(
                                text = if (correct) "✔" else "❌",
                                color = if (correct) Color.Green else Color.Red,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Mật khẩu mới") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = errorMessage != null,
                    trailingIcon = {
                        isNewPasswordValid?.let { valid ->
                            Text(
                                text = if (valid) "✔" else "❌",
                                color = if (valid) Color.Green else Color.Red,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Xác nhận mật khẩu") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = errorMessage != null,
                    trailingIcon = {
                        isConfirmPasswordValid?.let { valid ->
                            Text(
                                text = if (valid) "✔" else "❌",
                                color = if (valid) Color.Green else Color.Red,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                )
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            when {
                                oldPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank() -> {
                                    errorMessage = "Vui lòng điền đầy đủ thông tin"
                                }
                                oldPassword != currentUser.password -> {
                                    errorMessage = "Mật khẩu cũ không đúng"
                                }
                                newPassword.length < 6 -> {
                                    errorMessage = "Mật khẩu mới phải có ít nhất 6 ký tự"
                                }
                                newPassword != confirmPassword -> {
                                    errorMessage = "Mật khẩu xác nhận không khớp"
                                }
                                else -> {
                                    errorMessage = null
                                    onSave(newPassword)
                                }
                            }
                        }
                    ) {
                        Text("Lưu")
                    }
                }
            }
        }
    }
}

/**
 * Dialog for privacy settings (placeholder).
 */
@Composable
private fun PrivacySettingsDialog(
    onDismiss: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Cài đặt quyền riêng tư",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Chức năng đang phát triển. Bạn có muốn xóa tài khoản?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onDeleteAccount,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Xóa tài khoản")
                    }
                }
            }
        }
    }
}