package com.donglab.media3sample.data.repository

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.donglab.media3sample.data.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class MusicRepositoryImpl(
    context: Context
) : MusicRepository {

    private val exoPlayer = ExoPlayer.Builder(context).build()
    private val _currentTrack = MutableStateFlow<Track?>(null)
    private val _isPlaying = MutableStateFlow(false)
    private val _playbackPosition = MutableStateFlow(0L)
    private val _duration = MutableStateFlow(0L)

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                _isPlaying.value = exoPlayer.isPlaying
                when (playbackState) {
                    Player.STATE_READY -> {
                        _duration.value = exoPlayer.duration
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }
        })
    }

    override suspend fun getTracks(): List<Track> {
        return listOf(
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
            )
        )
    }

    override fun getCurrentTrack(): Flow<Track?> = _currentTrack.asStateFlow()

    override suspend fun playTrack(track: Track) {
        val mediaItem = MediaItem.fromUri(track.uri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
        _currentTrack.value = track
    }

    override suspend fun pause() {
        exoPlayer.pause()
    }

    override suspend fun resume() {
        exoPlayer.play()
    }

    override suspend fun stop() {
        exoPlayer.stop()
        _currentTrack.value = null
    }

    override suspend fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
    }

    override suspend fun skipToNext() {
        if (exoPlayer.hasNextMediaItem()) {
            exoPlayer.seekToNext()
        }
    }

    override suspend fun skipToPrevious() {
        if (exoPlayer.hasPreviousMediaItem()) {
            exoPlayer.seekToPrevious()
        }
    }

    override fun getPlaybackPosition(): Flow<Long> = flow {
        while (true) {
            emit(exoPlayer.currentPosition)
            kotlinx.coroutines.delay(1000)
        }
    }

    override fun isPlaying(): Flow<Boolean> = _isPlaying.asStateFlow()

    override fun getDuration(): Flow<Long> = _duration.asStateFlow()
}