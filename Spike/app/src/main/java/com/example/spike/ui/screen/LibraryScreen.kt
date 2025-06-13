package com.example.spike.ui.screen

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun LibraryScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("library_prefs", Context.MODE_PRIVATE)
    var savedSongs by remember { mutableStateOf(getSavedSongs(sharedPrefs)) }

    // Quản lý trạng thái cho từng bài hát (bài nào show icon xóa)
    var deleteStates by remember { mutableStateOf(savedSongs.associateWith { false }.toMutableMap()) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                addSong(sharedPrefs)
                savedSongs = getSavedSongs(sharedPrefs)
                deleteStates = savedSongs.associateWith { false }.toMutableMap()
            }) {
                Icon(Icons.Default.Add, contentDescription = "Thêm bài hát")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Thư viện của bạn",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF212121)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (savedSongs.isEmpty()) {
                Text("Chưa có bài hát nào.", fontSize = 16.sp)
            } else {
                LazyColumn {
                    items(savedSongs) { song ->
                        SongItem(
                            songTitle = song,
                            showDelete = deleteStates[song] == true,
                            onLongClick = {
                                deleteStates = deleteStates.mapValues { it.key == song }.toMutableMap()
                            },
                            onDelete = {
                                deleteSong(sharedPrefs, song)
                                savedSongs = getSavedSongs(sharedPrefs)
                                deleteStates = savedSongs.associateWith { false }.toMutableMap()
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongItem(
    songTitle: String,
    showDelete: Boolean,
    onLongClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .combinedClickable(
                onClick = { },
                onLongClick = { onLongClick() }
            ),
        colors = CardDefaults.cardColors(Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                songTitle,
                fontSize = 16.sp,
                color = Color(0xFF333333),
                modifier = Modifier.weight(1f)
            )
            if (showDelete) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Xóa bài hát", tint = Color.Red)
                }
            }
        }
    }
}

// ==== Xử lý dữ liệu SharedPreferences ====

// Đọc dữ liệu list bài hát
fun getSavedSongs(prefs: SharedPreferences): List<String> {
    val saved = prefs.getString("saved_songs_list", null)
    return saved?.split("|")?.filter { it.isNotBlank() } ?: emptyList()
}

// Ghi dữ liệu list bài hát
fun saveSongs(prefs: SharedPreferences, songs: List<String>) {
    val data = songs.joinToString("|")
    prefs.edit().putString("saved_songs_list", data).apply()
}

// Thêm bài hát mới
fun addSong(prefs: SharedPreferences) {
    val currentList = getSavedSongs(prefs).toMutableList()
    val newSong = "Bài hát ${currentList.size + 1}"
    currentList.add(newSong)
    saveSongs(prefs, currentList)
}

// Xóa bài hát & đánh lại số thứ tự
fun deleteSong(prefs: SharedPreferences, song: String) {
    val currentList = getSavedSongs(prefs).toMutableList()
    currentList.remove(song)
    val reOrdered = currentList.mapIndexed { index, _ -> "Bài hát ${index + 1}" }
    saveSongs(prefs, reOrdered)
}
