package com.example.spike.ui.screen

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.spike.R
import com.example.spike.data.Song
import com.example.spike.data.SongRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyColumn


// Lớp quản lý MediaPlayer để đảm bảo an toàn
class MediaPlayerManager {
    private var mediaPlayer: MediaPlayer? = null

    fun play(context: Context, audioRes: Int, onCompletion: () -> Unit) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, audioRes)?.apply {
                setOnCompletionListener {
                    onCompletion()
                    release()
                }
                start()
            }
        } catch (e: Exception) {
            // Log lỗi nếu cần
        }
    }

    fun pause() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } catch (e: IllegalStateException) {
            // Log lỗi nếu cần
        }
    }

    fun resume() {
        try {
            if (mediaPlayer?.isPlaying == false) {
                mediaPlayer?.start()
            }
        } catch (e: IllegalStateException) {
            // Log lỗi nếu cần
        }
    }

    fun release() {
        try {
            mediaPlayer?.release()
        } catch (e: Exception) {
            // Log lỗi nếu cần
        } finally {
            mediaPlayer = null
        }
    }

    fun isPlaying(): Boolean {
        return try {
            mediaPlayer?.isPlaying == true
        } catch (e: IllegalStateException) {
            false
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val songs = SongRepository.allSongs
    val filteredSongs = remember(searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else songs.filter { it.title.contains(searchQuery, ignoreCase = true) }
    }
    val mediaPlayerManager = remember { MediaPlayerManager() }
    var playingSong by remember { mutableStateOf<Song?>(null) }
    var playingIdx by remember { mutableStateOf<Int?>(null) }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var selectedPlaylist by remember { mutableStateOf<String?>(null) }

    val contentAlpha by animateFloatAsState(
        targetValue = if (isSearchActive || searchQuery.isNotBlank()) 0.3f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    DisposableEffect(Unit) {
        onDispose { mediaPlayerManager.release() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .navigationBarsPadding(), // đảm bảo cuộn đến sát edge
            contentPadding = PaddingValues(vertical = 1.dp),
        ) {
            item {
                TopBar(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    onSettingsClick = { navController.navigate("settings") },
                    focusRequester = focusRequester,
                    onFocusChanged = { isSearchActive = it || isSearchActive },
                    onSearch = {
                        isSearchActive = true
                        keyboardController?.hide()
                    }
                )
            }

            if (isSearchActive || searchQuery.isNotBlank()) {
                item {
                    if (searchQuery.isBlank()) {
                        Text(
                            text = "Hãy nhập từ khóa để tìm kiếm bài hát",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else if (filteredSongs.isEmpty()) {
                        Text(
                            text = "Không tìm thấy bài hát phù hợp",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        SongList(
                            title = "Kết quả tìm kiếm",
                            songs = filteredSongs,
                            mediaPlayerManager = mediaPlayerManager,
                            playingIdx = playingIdx,
                            playingSong = playingSong,
                            onPlayingSongChange = { playingSong = it },
                            onPlayingIdxChange = { playingIdx = it },
                            originalSongs = songs,
                            context = context
                        )
                    }
                }
            } else {
                item {
                    Column(modifier = Modifier.graphicsLayer(alpha = contentAlpha)) {
                        SuggestionTitle()
                        PlaylistSuggestions(
                            selectedPlaylist = selectedPlaylist,
                            onPlaylistSelected = { selectedPlaylist = it }
                        )
                    }
                }

                item {
                    SongList(
                        title = "Bài hát nổi bật",
                        songs = if (selectedPlaylist == null) songs else SongRepository.playlists[selectedPlaylist]
                            ?: emptyList(),
                        mediaPlayerManager = mediaPlayerManager,
                        playingIdx = playingIdx,
                        playingSong = playingSong,
                        onPlayingSongChange = { playingSong = it },
                        onPlayingIdxChange = { playingIdx = it },
                        originalSongs = songs,
                        context = context
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(72.dp)) // ✅ Đệm chống che bởi BottomNavigationBar
            }
        }

        playingSong?.let { currentSong ->
            MiniPlayer(
                currentSong = currentSong,
                isPlaying = mediaPlayerManager.isPlaying(),
                context = context,
                onPlayPauseClick = {
                    if (mediaPlayerManager.isPlaying()) mediaPlayerManager.pause()
                    else mediaPlayerManager.resume()
                },
                onNextClick = {
                    val allSongs = SongRepository.allSongs
                    val currentIndex = allSongs.indexOf(currentSong)
                    val nextIndex = (currentIndex + 1) % allSongs.size
                    val nextSong = allSongs[nextIndex]
                    mediaPlayerManager.play(context, nextSong.audioRes) {
                        playingSong = null
                        playingIdx = null
                    }
                    playingSong = nextSong
                    playingIdx = nextIndex
                },
                onPreviousClick = {
                    val allSongs = SongRepository.allSongs
                    val currentIndex = allSongs.indexOf(currentSong)
                    val prevIndex = if (currentIndex > 0) currentIndex - 1 else allSongs.size - 1
                    val prevSong = allSongs[prevIndex]
                    mediaPlayerManager.play(context, prevSong.audioRes) {
                        playingSong = null
                        playingIdx = null
                    }
                    playingSong = prevSong
                    playingIdx = prevIndex
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun TopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSettingsClick: () -> Unit,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    onSearch: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

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
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Bạn muốn nghe gì?", color = Color(0xFF757575)) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF757575))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                disabledContainerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { onFocusChanged(it.isFocused) }
        )
    }
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
fun PlaylistSuggestions(selectedPlaylist: String?, onPlaylistSelected: (String) -> Unit) {
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
            val isSelected = selectedPlaylist == name

            // Tính màu nền tùy vào trạng thái chọn
            val bgColor by animateColorAsState(
                targetValue = when {
                    selectedPlaylist == null -> colors[index] // Chưa chọn: sáng hết
                    isSelected -> colors[index].copy(alpha = 1f) // Đang chọn: sáng
                    else -> colors[index].copy(alpha = 0.4f) // Không chọn: mờ đi
                },
                animationSpec = tween(durationMillis = 300)
            )

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(bgColor, MaterialTheme.shapes.medium)
                    .clickable(enabled = true) { // Cho phép bấm tất cả
                        clicked = true
                        onPlaylistSelected(name)
                        scope.launch {
                            delay(300)
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
fun SongList(
    title: String,
    songs: List<Song>,
    mediaPlayerManager: MediaPlayerManager,
    playingIdx: Int?,
    playingSong: Song?,
    onPlayingSongChange: (Song?) -> Unit,
    onPlayingIdxChange: (Int?) -> Unit,
    originalSongs: List<Song>,
    context: Context
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = title,
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
                val isPlaying = originalSongs.getOrNull(playingIdx ?: -1) == song

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .background(Color(0xFFF0F0F0), MaterialTheme.shapes.medium)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(song.imageRes),
                        contentDescription = song.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )

                    Box(modifier = Modifier.height(50.dp)) {
                        Text(
                            text = song.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF212121),
                            maxLines = 2
                        )
                    }

                    IconButton(
                        onClick = {
                            val originalIndex = originalSongs.indexOf(song)
                            if (isPlaying) {
                                mediaPlayerManager.pause()
                                onPlayingIdxChange(null)
                                onPlayingSongChange(null)
                            } else {
                                mediaPlayerManager.play(context, song.audioRes) {
                                    onPlayingIdxChange(null)
                                    onPlayingSongChange(null)
                                }
                                onPlayingIdxChange(originalIndex)
                                onPlayingSongChange(song)
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

@Composable
fun MiniPlayer(
    currentSong: Song,
    isPlaying: Boolean,
    context: Context,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        color = Color(0xFFF5F5F5).copy(alpha = 0.8f),
        shape = MaterialTheme.shapes.small,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(currentSong.imageRes),
                contentDescription = currentSong.title,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Gray, MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop
            )

            Text(
                text = currentSong.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF212121),
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            Row {
                IconButton(onClick = onPreviousClick) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = Color(0xFF6200EE)
                    )
                }
                IconButton(onClick = onPlayPauseClick) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color(0xFF6200EE)
                    )
                }
                IconButton(onClick = onNextClick) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = Color(0xFF6200EE)
                    )
                }
            }
        }
    }
}