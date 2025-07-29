package com.donglab.media3sample.presentation.player.extensions.multiaudio

import com.donglab.media3sample.presentation.player.MediaPlayer
import com.donglab.media3sample.presentation.player.PlaybackState
import com.donglab.media3sample.presentation.player.PlayerEvent
import com.donglab.media3sample.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * 다중 음원 재생 기능을 추가하는 MediaPlayer Decorator
 */
class MultiAudioMediaPlayer(
    private val mediaPlayer: MediaPlayer,
    private val multiAudioComponent: MultiAudioComponent = DefaultMultiAudioComponent()
) : MediaPlayer {
    
    init {
        // 기본 플레이어를 메인 플레이어로 설정
        multiAudioComponent.setMainPlayer(mediaPlayer)
    }
    
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
    
    override fun release() {
        mediaPlayer.release()
        multiAudioComponent.release()
    }
    
    // 다중 음원 전용 기능들
    val activePlayerId: StateFlow<String?> = multiAudioComponent.activePlayerId
    val mainPlayer: MediaPlayer? get() = multiAudioComponent.mainPlayer
    val subPlayers: Map<String, MediaPlayer> get() = multiAudioComponent.subPlayers
    
    fun addSubPlayer(id: String, player: MediaPlayer) {
        multiAudioComponent.addSubPlayer(id, player)
    }
    
    fun removeSubPlayer(id: String) {
        multiAudioComponent.removeSubPlayer(id)
    }
    
    fun setActivePlayer(playerId: String?) {
        multiAudioComponent.setActivePlayer(playerId)
    }
    
    fun getPlayer(playerId: String): MediaPlayer? {
        return multiAudioComponent.getPlayer(playerId)
    }
    
    fun playTrackOnPlayer(playerId: String, track: Track) {
        multiAudioComponent.playTrackOnPlayer(playerId, track)
    }
    
    fun pausePlayer(playerId: String) {
        multiAudioComponent.pausePlayer(playerId)
    }
    
    fun resumePlayer(playerId: String) {
        multiAudioComponent.resumePlayer(playerId)
    }
    
    fun stopPlayer(playerId: String) {
        multiAudioComponent.stopPlayer(playerId)
    }
    
    fun setPlayerVolume(playerId: String, volume: Float) {
        multiAudioComponent.setPlayerVolume(playerId, volume)
    }
    
    fun setPlayerSpeed(playerId: String, speed: Float) {
        multiAudioComponent.setPlayerSpeed(playerId, speed)
    }
    
    fun seekPlayerTo(playerId: String, position: Long) {
        multiAudioComponent.seekPlayerTo(playerId, position)
    }
    
    fun pauseAll() {
        multiAudioComponent.pauseAll()
    }
    
    fun stopAll() {
        multiAudioComponent.stopAll()
    }
    
    fun setVolumeForAll(volume: Float) {
        multiAudioComponent.setVolumeForAll(volume)
    }
    
    fun setPlaybackSpeedForAll(speed: Float) {
        multiAudioComponent.setPlaybackSpeedForAll(speed)
    }
}