package com.donglab.media3sample.presentation.player.extensions.multiaudio

import com.donglab.media3sample.presentation.player.MediaPlayer
import com.donglab.media3sample.domain.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MultiAudioManager {
    
    private var _mainPlayer: MediaPlayer? = null
    val mainPlayer: MediaPlayer? get() = _mainPlayer
    
    private val _subPlayers = mutableMapOf<String, MediaPlayer>()
    val subPlayers: Map<String, MediaPlayer> get() = _subPlayers.toMap()
    
    private val _activePlayerId = MutableStateFlow<String?>(null)
    val activePlayerId: StateFlow<String?> = _activePlayerId.asStateFlow()
    
    
    fun setMainPlayer(player: MediaPlayer) {
        _mainPlayer?.release()
        _mainPlayer = player
        if (_activePlayerId.value == null) {
            _activePlayerId.value = MAIN_PLAYER_ID
        }
    }
    
    fun addSubPlayer(id: String, player: MediaPlayer) {
        require(id != MAIN_PLAYER_ID) { "Cannot use reserved ID: $MAIN_PLAYER_ID" }
        _subPlayers[id]?.release()
        _subPlayers[id] = player
    }
    
    fun removeSubPlayer(id: String) {
        _subPlayers[id]?.release()
        _subPlayers.remove(id)
        
        // 활성 플레이어가 제거된 경우 메인 플레이어로 전환
        if (_activePlayerId.value == id) {
            _activePlayerId.value = if (_mainPlayer != null) MAIN_PLAYER_ID else null
        }
    }
    
    fun setActivePlayer(playerId: String?) {
        when (playerId) {
            MAIN_PLAYER_ID -> {
                if (_mainPlayer != null) {
                    _activePlayerId.value = MAIN_PLAYER_ID
                }
            }
            null -> {
                _activePlayerId.value = null
            }
            else -> {
                if (_subPlayers.containsKey(playerId)) {
                    _activePlayerId.value = playerId
                }
            }
        }
    }
    
    fun getPlayer(playerId: String): MediaPlayer? {
        return when (playerId) {
            MAIN_PLAYER_ID -> _mainPlayer
            else -> _subPlayers[playerId]
        }
    }
    
    
    // 모든 플레이어 제어 메서드들
    
    fun pauseAll() {
        _mainPlayer?.pause()
        _subPlayers.values.forEach { it.pause() }
    }
    
    fun stopAll() {
        _mainPlayer?.stop()
        _subPlayers.values.forEach { it.stop() }
    }
    
    fun setVolumeForAll(volume: Float) {
        _mainPlayer?.setVolume(volume)
        _subPlayers.values.forEach { it.setVolume(volume) }
    }
    
    fun setPlaybackSpeedForAll(speed: Float) {
        _mainPlayer?.setPlaybackSpeed(speed)
        _subPlayers.values.forEach { it.setPlaybackSpeed(speed) }
    }
    
    // 개별 플레이어 제어 메서드들
    
    fun playTrackOnPlayer(playerId: String, track: Track) {
        getPlayer(playerId)?.playTrack(track)
        setActivePlayer(playerId)
    }
    
    fun pausePlayer(playerId: String) {
        getPlayer(playerId)?.pause()
    }
    
    fun resumePlayer(playerId: String) {
        getPlayer(playerId)?.resume()
    }
    
    fun stopPlayer(playerId: String) {
        getPlayer(playerId)?.stop()
    }
    
    fun setPlayerVolume(playerId: String, volume: Float) {
        getPlayer(playerId)?.setVolume(volume)
    }
    
    fun setPlayerSpeed(playerId: String, speed: Float) {
        getPlayer(playerId)?.setPlaybackSpeed(speed)
    }
    
    fun seekPlayerTo(playerId: String, position: Long) {
        getPlayer(playerId)?.seekTo(position)
    }
    
    
    fun release() {
        _mainPlayer?.release()
        _subPlayers.values.forEach { it.release() }
        _subPlayers.clear()
        _mainPlayer = null
        _activePlayerId.value = null
    }
    
    companion object {
        const val MAIN_PLAYER_ID = "main"
    }
}