package com.donglab.media3sample.presentation.player.extensions.playbackmode

import com.donglab.media3sample.presentation.player.MediaPlayer
import com.donglab.media3sample.presentation.player.PlaybackState
import com.donglab.media3sample.presentation.player.PlayerEvent
import com.donglab.media3sample.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * 재생 모드 기능을 추가하는 MediaPlayer Decorator
 * PlaylistMediaPlayer와 함께 사용될 때 더 유용함
 */
class PlaybackModeMediaPlayer(
    private val mediaPlayer: MediaPlayer,
    private val playbackModeComponent: PlaybackModeComponent = DefaultPlaybackModeComponent()
) : MediaPlayer {
    
    // 기본 MediaPlayer 기능 위임
    override val playbackState: PlaybackState get() = mediaPlayer.playbackState
    override val playerEvents: Flow<PlayerEvent> get() = mediaPlayer.playerEvents
    override val currentPosition: Long get() = mediaPlayer.currentPosition
    override val duration: Long get() = mediaPlayer.duration
    override val isPlaying: Boolean get() = mediaPlayer.isPlaying
    override val currentTrack: Track? get() = mediaPlayer.currentTrack
    
    override fun playTrack(track: Track) = mediaPlayer.playTrack(track)
    override fun pause() = mediaPlayer.pause()
    override fun resume() = mediaPlayer.resume()
    override fun stop() = mediaPlayer.stop()
    override fun seekTo(millis: Long) = mediaPlayer.seekTo(millis)
    override fun setVolume(volume: Float) = mediaPlayer.setVolume(volume)
    override fun setPlaybackSpeed(speed: Float) = mediaPlayer.setPlaybackSpeed(speed)
    override fun release() = mediaPlayer.release()
    
    // 재생 모드 전용 기능들
    val playbackMode: StateFlow<PlaybackMode> = playbackModeComponent.playbackMode
    
    fun setPlaybackMode(mode: PlaybackMode) {
        playbackModeComponent.setPlaybackMode(mode)
    }
    
    /**
     * PlaylistMediaPlayer와 함께 사용될 때의 다음 트랙 가져오기
     * 재생 모드에 따라 동작이 달라짐
     */
    fun getNextTrack(playlist: List<Track>, currentIndex: Int): Track? {
        return playbackModeComponent.getNextTrack(currentTrack, playlist, currentIndex)
    }
    
    /**
     * PlaylistMediaPlayer와 함께 사용될 때의 이전 트랙 가져오기
     * 재생 모드에 따라 동작이 달라짐
     */
    fun getPreviousTrack(playlist: List<Track>, currentIndex: Int): Track? {
        return playbackModeComponent.getPreviousTrack(currentTrack, playlist, currentIndex)
    }
}