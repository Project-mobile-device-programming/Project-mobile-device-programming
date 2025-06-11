package com.example.spike.ui.screen

import android.media.MediaPlayer
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spike.R
import kotlinx.coroutines.launch
import androidx.navigation.NavHostController
import com.example.spike.ui.components.BottomNavigationBar
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

data class Song(val title: String, val imageRes: Int, val audioRes: Int)

@Composable
fun HomeScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }

    val songs = listOf(
        Song("Trúc Xinh", R.drawable.song1, R.raw.song1),
        Song("Đừng làm trái tim anh đau", R.drawable.song2, R.raw.song2),
        Song("Hãy trao cho anh", R.drawable.song3, R.raw.song3),
        Song("Mất kết nối", R.drawable.song4, R.raw.song4),
    )

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)
                .padding(bottom = 80.dp)
        ) {
            // Cả 2 đều dùng cùng biến và logic
            TopBar(
                onSearchClick = {
                    navController.navigate("search") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onSettingsClick = {
                    navController.navigate("settings")
                }
            )

            SuggestionTitle()
            PlaylistSuggestions()
            SongList(songs = songs)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TopBar(
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
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

            Row {
                IconButton(onClick = { /* thông báo */ }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Thông báo",
                        tint = Color(0xFF212121)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), shape = MaterialTheme.shapes.small)
                .clickable { onSearchClick() }
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF757575)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Bạn muốn nghe gì?", color = Color(0xFF757575), fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Bạn muốn nghe gì?", color = Color(0xFF757575)) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF757575))
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF5F5F5),
            unfocusedContainerColor = Color(0xFFF5F5F5),
            disabledContainerColor = Color(0xFFF5F5F5),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onSearchClick() } // Click toàn khối chuyển trang
    )
}

@Composable
fun SuggestionTitle() {
    Text(
        text = "Gợi ý cho bạn",
        color = Color(0xFF212121),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun PlaylistSuggestions() {
    val playlists = listOf("Top Hits", "Chill Vibes", "Workout", "Ballads", "EDM", "Remix")
    val colors = listOf(
        Color(0xFFFF6F61), Color(0xFF6BCB77), Color(0xFF4D96FF),
        Color(0xFFF9DC5C), Color(0xFFFF9F1C), Color(0xFF9B5DE5)
    )
    val scope = rememberCoroutineScope()

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(playlists) { index, name ->
            var clicked by remember { mutableStateOf(false) }
            val bgColor by animateColorAsState(
                if (clicked) colors[index].copy(alpha = 0.7f)
                else colors[index]
            )
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(bgColor, MaterialTheme.shapes.medium)
                    .clickable {
                        clicked = true
                        scope.launch {
                            kotlinx.coroutines.delay(300)
                            clicked = false
                        }
                    }
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun SongList(songs: List<Song>) {
    val ctx = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var playingIdx by remember { mutableStateOf<Int?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Bài hát nổi bật",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp)
        ) {
            itemsIndexed(songs) { index, song ->
                val isPlaying = (playingIdx == index)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(Color(0xFFF0F0F0), MaterialTheme.shapes.medium)
                        .padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(song.imageRes),
                        contentDescription = song.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = song.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF212121),
                        maxLines = 2,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            if (isPlaying) {
                                mediaPlayer?.pause()
                                playingIdx = null
                            } else {
                                mediaPlayer?.release()
                                mediaPlayer = MediaPlayer.create(ctx, song.audioRes).apply {
                                    setOnCompletionListener { playingIdx = null }
                                    start()
                                }
                                playingIdx = index
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            tint = Color(0xFF6200EE),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}
