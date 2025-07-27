package com.donglab.media3sample.data.repository

import com.donglab.media3sample.data.model.Track

class MusicRepositoryImpl : MusicRepository {

    private val sampleTracks = listOf(
        Track(
            id = "1",
            title = "Sample Song 1",
            artist = "Sample Artist 1",
            album = "Sample Album 1",
            duration = 180000L,
            uri = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
        ),
        Track(
            id = "2",
            title = "Sample Song 2",
            artist = "Sample Artist 2", 
            album = "Sample Album 2",
            duration = 200000L,
            uri = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"
        ),
        Track(
            id = "3",
            title = "Sample Song 3",
            artist = "Sample Artist 3",
            album = "Sample Album 3", 
            duration = 220000L,
            uri = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"
        ),
        Track(
            id = "4",
            title = "Relaxing Music",
            artist = "Calm Artists",
            album = "Peaceful Sounds",
            duration = 240000L,
            uri = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3"
        ),
        Track(
            id = "5",
            title = "Upbeat Track",
            artist = "Energy Band",
            album = "High Energy",
            duration = 195000L,
            uri = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3"
        )
    )

    override suspend fun getTracks(): List<Track> {
        // 실제 구현에서는 네트워크 API, 로컬 DB, 파일 시스템 등에서 데이터를 가져옴
        return sampleTracks
    }

    override suspend fun getTrackById(id: String): Track? {
        return sampleTracks.find { it.id == id }
    }

    override suspend fun searchTracks(query: String): List<Track> {
        return sampleTracks.filter { track ->
            track.title.contains(query, ignoreCase = true) ||
            track.artist.contains(query, ignoreCase = true) ||
            track.album.contains(query, ignoreCase = true)
        }
    }
}