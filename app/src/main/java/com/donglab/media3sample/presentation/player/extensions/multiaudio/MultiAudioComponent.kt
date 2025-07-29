package com.donglab.media3sample.presentation.player.extensions.multiaudio

import com.donglab.media3sample.presentation.player.MediaPlayer
import com.donglab.media3sample.domain.model.Track
import kotlinx.coroutines.flow.StateFlow

interface MultiAudioComponent {
    val activePlayerId: StateFlow<String?>
    val mainPlayer: MediaPlayer?
    val subPlayers: Map<String, MediaPlayer>
    
    fun setMainPlayer(player: MediaPlayer)
    fun addSubPlayer(id: String, player: MediaPlayer)
    fun removeSubPlayer(id: String)
    fun setActivePlayer(playerId: String?)
    fun getPlayer(playerId: String): MediaPlayer?
    
    // 모든 플레이어 제어
    fun pauseAll()
    fun stopAll()
    fun setVolumeForAll(volume: Float)
    fun setPlaybackSpeedForAll(speed: Float)
    
    // 개별 플레이어 제어
    fun playTrackOnPlayer(playerId: String, track: Track)
    fun pausePlayer(playerId: String)
    fun resumePlayer(playerId: String)
    fun stopPlayer(playerId: String)
    fun setPlayerVolume(playerId: String, volume: Float)
    fun setPlayerSpeed(playerId: String, speed: Float)
    fun seekPlayerTo(playerId: String, position: Long)
    
    fun release()
}

class DefaultMultiAudioComponent : MultiAudioComponent {
    
    private val multiAudioManager = MultiAudioManager()
    
    override val activePlayerId: StateFlow<String?> = multiAudioManager.activePlayerId
    override val mainPlayer: MediaPlayer? get() = multiAudioManager.mainPlayer
    override val subPlayers: Map<String, MediaPlayer> get() = multiAudioManager.subPlayers
    
    override fun setMainPlayer(player: MediaPlayer) {
        multiAudioManager.setMainPlayer(player)
    }
    
    override fun addSubPlayer(id: String, player: MediaPlayer) {
        multiAudioManager.addSubPlayer(id, player)
    }
    
    override fun removeSubPlayer(id: String) {
        multiAudioManager.removeSubPlayer(id)
    }
    
    override fun setActivePlayer(playerId: String?) {
        multiAudioManager.setActivePlayer(playerId)
    }
    
    override fun getPlayer(playerId: String): MediaPlayer? {
        return multiAudioManager.getPlayer(playerId)
    }
    
    override fun pauseAll() {
        multiAudioManager.pauseAll()
    }
    
    override fun stopAll() {
        multiAudioManager.stopAll()
    }
    
    override fun setVolumeForAll(volume: Float) {
        multiAudioManager.setVolumeForAll(volume)
    }
    
    override fun setPlaybackSpeedForAll(speed: Float) {
        multiAudioManager.setPlaybackSpeedForAll(speed)
    }
    
    override fun playTrackOnPlayer(playerId: String, track: Track) {
        multiAudioManager.playTrackOnPlayer(playerId, track)
    }
    
    override fun pausePlayer(playerId: String) {
        multiAudioManager.pausePlayer(playerId)
    }
    
    override fun resumePlayer(playerId: String) {
        multiAudioManager.resumePlayer(playerId)
    }
    
    override fun stopPlayer(playerId: String) {
        multiAudioManager.stopPlayer(playerId)
    }
    
    override fun setPlayerVolume(playerId: String, volume: Float) {
        multiAudioManager.setPlayerVolume(playerId, volume)
    }
    
    override fun setPlayerSpeed(playerId: String, speed: Float) {
        multiAudioManager.setPlayerSpeed(playerId, speed)
    }
    
    override fun seekPlayerTo(playerId: String, position: Long) {
        multiAudioManager.seekPlayerTo(playerId, position)
    }
    
    override fun release() {
        multiAudioManager.release()
    }
}