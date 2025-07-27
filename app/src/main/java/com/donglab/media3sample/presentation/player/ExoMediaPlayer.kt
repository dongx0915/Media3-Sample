package com.donglab.media3sample.presentation.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.donglab.media3sample.domain.model.Track
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

/**
 * ExoPlayer를 사용한 MediaPlayer 구현체
 * 다른 프로젝트에서도 쉽게 사용할 수 있도록 Context만 주입받아 동작
 */
class ExoMediaPlayer(context: Context) : MediaPlayer {
    
    private val exoPlayer = ExoPlayer.Builder(context).build()
    private val eventChannel = Channel<PlayerEvent>(Channel.UNLIMITED)
    private var playlist: List<Track> = emptyList()
    private var currentTrackInternal: Track? = null
    
    override val playerEvents: Flow<PlayerEvent> = eventChannel.receiveAsFlow()
    
    override val currentPosition: Long
        get() = exoPlayer.currentPosition
    
    override val duration: Long
        get() = exoPlayer.duration.let { if (it == -1L) 0L else it }
    
    override val playbackState: PlaybackState
        get() = when (exoPlayer.playbackState) {
            Player.STATE_IDLE -> PlaybackState.IDLE
            Player.STATE_BUFFERING -> PlaybackState.LOADING
            Player.STATE_READY -> if (exoPlayer.isPlaying) PlaybackState.PLAYING else PlaybackState.READY
            Player.STATE_ENDED -> PlaybackState.ENDED
            else -> PlaybackState.IDLE
        }
    
    override val isPlaying: Boolean
        get() = exoPlayer.isPlaying
    
    override val currentTrack: Track?
        get() = currentTrackInternal
    
    init {
        setupExoPlayerListener()
    }
    
    private fun setupExoPlayerListener() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                val state = when (playbackState) {
                    Player.STATE_IDLE -> PlaybackState.IDLE
                    Player.STATE_BUFFERING -> PlaybackState.LOADING
                    Player.STATE_READY -> PlaybackState.READY
                    Player.STATE_ENDED -> PlaybackState.ENDED
                    else -> PlaybackState.IDLE
                }
                eventChannel.trySend(PlayerEvent.PlaybackStateChanged(state))
                
                if (playbackState == Player.STATE_READY) {
                    eventChannel.trySend(PlayerEvent.DurationChanged(duration))
                }
            }
            
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                eventChannel.trySend(PlayerEvent.IsPlayingChanged(isPlaying))
            }
            
            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                eventChannel.trySend(
                    PlayerEvent.Error(
                        message = error.message ?: "Unknown playback error",
                        cause = error
                    )
                )
            }
            
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val track = mediaItem?.let { findTrackByMediaItem(it) }
                currentTrackInternal = track
                eventChannel.trySend(PlayerEvent.TrackChanged(track))
            }
        })
        
        // 재생 위치 업데이트를 위한 별도 플로우
        startPositionUpdates()
    }
    
    private fun startPositionUpdates() {
        // 별도 코루틴에서 position 업데이트 (ViewModel에서 수집)
    }
    
    private fun findTrackByMediaItem(mediaItem: MediaItem): Track? {
        return playlist.find { it.uri == mediaItem.localConfiguration?.uri.toString() }
    }
    
    override suspend fun playTrack(track: Track) {
        try {
            val mediaItem = MediaItem.fromUri(track.uri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
            currentTrackInternal = track
            eventChannel.send(PlayerEvent.TrackChanged(track))
        } catch (e: Exception) {
            eventChannel.send(PlayerEvent.Error("Failed to play track: ${track.title}", e))
        }
    }
    
    override suspend fun pause() {
        exoPlayer.pause()
    }
    
    override suspend fun resume() {
        exoPlayer.play()
    }
    
    override suspend fun stop() {
        exoPlayer.stop()
        currentTrackInternal = null
        eventChannel.send(PlayerEvent.TrackChanged(null))
    }
    
    override suspend fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
    }
    
    override suspend fun skipToNext() {
        if (exoPlayer.hasNextMediaItem()) {
            exoPlayer.seekToNext()
        } else {
            // 플레이리스트에서 다음 트랙 찾기
            currentTrackInternal?.let { current ->
                val currentIndex = playlist.indexOfFirst { it.id == current.id }
                if (currentIndex != -1 && currentIndex < playlist.size - 1) {
                    playTrack(playlist[currentIndex + 1])
                }
            }
        }
    }
    
    override suspend fun skipToPrevious() {
        if (exoPlayer.hasPreviousMediaItem()) {
            exoPlayer.seekToPrevious()
        } else {
            // 플레이리스트에서 이전 트랙 찾기
            currentTrackInternal?.let { current ->
                val currentIndex = playlist.indexOfFirst { it.id == current.id }
                if (currentIndex > 0) {
                    playTrack(playlist[currentIndex - 1])
                }
            }
        }
    }
    
    override suspend fun setPlaylist(tracks: List<Track>, startIndex: Int) {
        playlist = tracks
        if (tracks.isNotEmpty() && startIndex in tracks.indices) {
            val mediaItems = tracks.map { MediaItem.fromUri(it.uri) }
            exoPlayer.setMediaItems(mediaItems, startIndex, 0)
            exoPlayer.prepare()
            currentTrackInternal = tracks[startIndex]
            eventChannel.send(PlayerEvent.TrackChanged(tracks[startIndex]))
        }
    }
    
    override suspend fun setVolume(volume: Float) {
        exoPlayer.volume = volume.coerceIn(0f, 1f)
    }
    
    override suspend fun setPlaybackSpeed(speed: Float) {
        exoPlayer.setPlaybackSpeed(speed.coerceIn(0.5f, 2.0f))
    }
    
    override suspend fun release() {
        exoPlayer.release()
        eventChannel.close()
    }
    
    /**
     * 재생 위치를 실시간으로 업데이트하는 Flow
     */
    fun getPositionUpdates(): Flow<Long> = flow {
        while (true) {
            if (isPlaying) {
                emit(currentPosition)
                eventChannel.trySend(PlayerEvent.PositionChanged(currentPosition))
            }
            delay(1000) // 1초마다 업데이트
        }
    }
}