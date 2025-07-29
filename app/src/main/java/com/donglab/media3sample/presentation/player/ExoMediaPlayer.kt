package com.donglab.media3sample.presentation.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.donglab.media3sample.domain.model.Track
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.cancel

/**
 * ExoPlayer를 사용한 MediaPlayer 구현체
 * 다른 프로젝트에서도 쉽게 사용할 수 있도록 Context만 주입받아 동작
 */
class ExoMediaPlayer(context: Context) : MediaPlayer {
    
    private val exoPlayer = ExoPlayer.Builder(context).build()
    private val playerScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var currentTrackInternal: Track? = null

    // Player 상태를 전송하는 Channel
    private val eventChannel = Channel<PlayerEvent>(Channel.UNLIMITED)
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

    private fun sendEvent(event: PlayerEvent) {
        playerScope.launch {
            eventChannel.send(event)
        }
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
                sendEvent(PlayerEvent.PlaybackStateChanged(state))
            }
            
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                sendEvent(PlayerEvent.IsPlayingChanged(isPlaying))
            }

            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                sendEvent(
                    PlayerEvent.Error(
                        message = error.message ?: "Unknown playback error",
                        cause = error
                    )
                )
            }
        })
    }
    
    override fun playTrack(track: Track) {
        currentTrackInternal = track

        try {
            val mediaItem = MediaItem.fromUri(track.uri)
            exoPlayer.apply {
                setMediaItem(mediaItem)
                prepare()
                play()
            }
        } catch (e: Exception) {
            sendEvent(PlayerEvent.Error("Failed to play track: ${track.title}", e))
        }
    }
    
    override fun pause() {
        exoPlayer.pause()
    }
    
    override fun resume() {
        exoPlayer.play()
    }
    
    override fun stop() {
        exoPlayer.stop()
        currentTrackInternal = null
    }
    
    override fun seekTo(millis: Long) {
        exoPlayer.seekTo(millis)
    }

    override fun setVolume(volume: Float) {
        exoPlayer.volume = volume.coerceIn(0f, 1f)
    }
    
    override fun setPlaybackSpeed(speed: Float) {
        exoPlayer.setPlaybackSpeed(speed.coerceIn(0.5f, 2.0f))
    }
    
    override fun release() {
        exoPlayer.release()
        eventChannel.close()
        playerScope.cancel()
    }
}