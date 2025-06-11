package com.example.spike.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spike.R
import com.example.spike.data.SongRepository
import com.example.spike.ui.components.SongList

@Composable
fun SearchScreen() {
    var searchQuery by remember { mutableStateOf("") }
    val allSongs = SongRepository.allSongs

    val filteredSongs = remember(searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else allSongs.filter { it.title.contains(searchQuery, ignoreCase = true) }
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(40.dp)
            )

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Thông báo",
                tint = Color(0xFF212121)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Bạn muốn nghe gì?", color = Color(0xFF757575)) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF757575))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                disabledContainerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (searchQuery.isBlank()) {
            Text(
                text = "Hãy nhập từ khóa để tìm kiếm bài hát",
                fontSize = 16.sp,
                color = Color.Gray
            )
        } else if (filteredSongs.isEmpty()) {
            Text(
                text = "Không tìm thấy bài hát phù hợp",
                fontSize = 16.sp,
                color = Color.Gray
            )
        } else {
            SongList(songs = filteredSongs)
        }
    }
}
