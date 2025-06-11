package com.example.spike.ui.components

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
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
import com.example.spike.data.Song

@Composable
fun SongList(
    songs: List<Song>,
    modifier: Modifier = Modifier
) {
    val ctx = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var playingIdx by remember { mutableStateOf<Int?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
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
