package com.example.spike.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavHostController, modifier: Modifier = Modifier) {
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route

    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
        contentColor = Color.Black,
        tonalElevation = 0.dp // Loại bỏ elevation để không có shadow/tint
    ) {
        val items = listOf(
            Triple(Icons.Default.Home, "Trang chủ", "home"),
            Triple(Icons.Default.Search, "Tìm kiếm", "search"),
            Triple(Icons.Default.LibraryMusic, "Thư viện", "library"),
            Triple(Icons.Default.Settings, "Cài đặt", "settings")
        )

        items.forEach { (icon, label, route) ->
            val selected = currentRoute == route
            val selColor = if (selected) Color(0xFF6200EE) else Color(0xFF9E9E9E)

            NavigationBarItem(
                icon = { Icon(icon, contentDescription = label, tint = selColor) },
                label = { Text(label, color = selColor, fontSize = 12.sp) },
                selected = selected,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo("home") {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF6200EE),
                    selectedTextColor = Color(0xFF6200EE),
                    unselectedIconColor = Color(0xFF9E9E9E),
                    unselectedTextColor = Color(0xFF9E9E9E),
                    indicatorColor = Color(0xFF6200EE).copy(alpha = 0.1f) // Màu tím nhạt cho indicator
                )
            )
        }
    }
}