package com.example.spike.data

import com.example.spike.R

object SongRepository {
    val allSongs = listOf(
        Song("Trúc Xinh", R.drawable.song1, R.raw.song1),
        Song("Đừng làm trái tim anh đau", R.drawable.song2, R.raw.song2),
        Song("Hãy trao cho anh", R.drawable.song3, R.raw.song3),
        Song("Mất kết nối", R.drawable.song4, R.raw.song4),
        Song("Track 1", R.drawable.hit1, R.raw.track1),
        Song("Track 2", R.drawable.hit2, R.raw.track2),
        Song("Track 3", R.drawable.hit3, R.raw.track3),
        Song("Track 4", R.drawable.hit4, R.raw.track4),
        Song("Track 5", R.drawable.hit5, R.raw.track5),
        Song("Track 6", R.drawable.hit6, R.raw.track6),
        Song("Track 7", R.drawable.hit7, R.raw.track7),
        Song("Track 8", R.drawable.hit8, R.raw.track8),
        Song("Track 9", R.drawable.hit9, R.raw.track9),
        Song("Track 10", R.drawable.hit10, R.raw.track10)
    )

    val playlists: Map<String, List<Song>> = mapOf(
        "Top Hits" to listOf(allSongs[0], allSongs[1], allSongs[2], allSongs[4], allSongs[5]),
        "Chill Vibes" to listOf(allSongs[3], allSongs[6], allSongs[7], allSongs[8]),
        "Workout" to listOf(allSongs[9], allSongs[10], allSongs[11]),
        "Ballads" to listOf(allSongs[0], allSongs[3], allSongs[12]),
        "EDM" to listOf(allSongs[13], allSongs[14]),
        "Remix" to listOf(allSongs[2], allSongs[5], allSongs[8], allSongs[11])
    )
}
