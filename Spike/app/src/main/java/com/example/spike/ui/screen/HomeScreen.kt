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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spike.R
import com.example.spike.data.Song
import com.example.spike.data.SongRepository
import kotlinx.coroutines.launch
import androidx.navigation.NavHostController
import androidx.compose.ui.graphics.graphicsLayer

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
    val scrollState = rememberScrollState()
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var playingIdx by remember { mutableStateOf<Int?>(null) }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Animate alpha for default content
    val contentAlpha by animateFloatAsState(
        targetValue = if (isSearchActive || searchQuery.isNotBlank()) 0.3f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)
                .padding(bottom = 72.dp) // Space for MiniPlayer and padding
        ) {
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
            // Always show search results or prompt when search is active
            if (isSearchActive || searchQuery.isNotBlank()) {
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
                        songs = filteredSongs,
                        mediaPlayer = mediaPlayer,
                        playingIdx = playingIdx,
                        onMediaPlayerChange = { mediaPlayer = it },
                        onPlayingIdxChange = { playingIdx = it },
                        isFiltered = true,
                        originalSongs = songs
                    )
                }
            }
            // Default content with animated alpha
            Column(
                modifier = Modifier.graphicsLayer(alpha = contentAlpha)
            ) {
                if (searchQuery.isBlank() && !isSearchActive) {
                    // Only show default content when search is not active
                    SuggestionTitle()
                    PlaylistSuggestions()
                    SongList(
                        songs = songs,
                        mediaPlayer = mediaPlayer,
                        playingIdx = playingIdx,
                        onMediaPlayerChange = { mediaPlayer = it },
                        onPlayingIdxChange = { playingIdx = it },
                        isFiltered = false
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Mini Player
        val currentIdx = playingIdx
        if (currentIdx != null && currentIdx in songs.indices) {
            MiniPlayer(
                currentSong = songs[currentIdx],
                isPlaying = mediaPlayer?.isPlaying == true,
                context = context,
                onPlayPauseClick = {
                    if (mediaPlayer?.isPlaying == true) {
                        mediaPlayer?.pause()
                        playingIdx = playingIdx // Trigger recomposition
                    } else {
                        mediaPlayer?.start()
                        playingIdx = playingIdx // Trigger recomposition
                    }
                },
                onNextClick = {
                    mediaPlayer?.release()
                    val nextIdx = if (currentIdx < songs.size - 1) currentIdx + 1 else 0
                    mediaPlayer = MediaPlayer.create(context, songs[nextIdx].audioRes).apply {
                        setOnCompletionListener { playingIdx = null }
                        start()
                    }
                    playingIdx = nextIdx
                },
                onPreviousClick = {
                    mediaPlayer?.release()
                    val prevIdx = if (currentIdx > 0) currentIdx - 1 else songs.size - 1
                    mediaPlayer = MediaPlayer.create(context, songs[prevIdx].audioRes).apply {
                        setOnCompletionListener { playingIdx = null }
                        start()
                    }
                    playingIdx = prevIdx
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp) // Match SearchBar padding
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
                if (clicked) colors[index].copy(alpha = 0.7f) else colors[index]
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
fun SongList(
    songs: List<Song>,
    mediaPlayer: MediaPlayer?,
    playingIdx: Int?,
    onMediaPlayerChange: (MediaPlayer?) -> Unit,
    onPlayingIdxChange: (Int?) -> Unit,
    isFiltered: Boolean = false,
    originalSongs: List<Song> = emptyList()
) {
    val ctx = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = if (isFiltered) "Kết quả tìm kiếm" else "Bài hát nổi bật",
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
                val isPlaying = if (isFiltered) {
                    val originalIndex = originalSongs.indexOf(song)
                    playingIdx == originalIndex
                } else {
                    playingIdx == index
                }
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
                            val targetIdx = if (isFiltered) originalSongs.indexOf(song) else index
                            if (isPlaying) {
                                mediaPlayer?.pause()
                                onPlayingIdxChange(null)
                            } else {
                                mediaPlayer?.release()
                                val newMediaPlayer = MediaPlayer.create(ctx, song.audioRes).apply {
                                    setOnCompletionListener { onPlayingIdxChange(null) }
                                    start()
                                }
                                onMediaPlayerChange(newMediaPlayer)
                                onPlayingIdxChange(targetIdx)
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
            .height(56.dp), // Match SearchBar height
        color = Color(0xFFF5F5F5).copy(alpha = 0.8f), // Translucent background
        shape = MaterialTheme.shapes.small,
        tonalElevation = 4.dp // Add subtle shadow
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Song Image
            Image(
                painter = painterResource(currentSong.imageRes),
                contentDescription = currentSong.title,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Gray, MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop
            )

            // Song Title
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

            // Control Buttons
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