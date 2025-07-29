package com.donglab.media3sample.presentation.player.extensions.playbackmode

import com.donglab.media3sample.domain.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class PlaybackModeManager {
    
    private val _playbackMode = MutableStateFlow(PlaybackMode.NORMAL)
    val playbackMode: StateFlow<PlaybackMode> = _playbackMode.asStateFlow()
    
    private var shuffleIndices: List<Int> = emptyList()
    private var currentShuffleIndex = -1
    
    fun setPlaybackMode(mode: PlaybackMode) {
        _playbackMode.value = mode
        
        // 셔플 모드로 변경 시 셔플 인덱스 초기화
        if (mode == PlaybackMode.SHUFFLE) {
            resetShuffle()
        }
    }
    
    fun getNextTrack(
        currentTrack: Track?,
        playlist: List<Track>,
        currentIndex: Int
    ): Track? {
        if (playlist.isEmpty()) return null
        
        return when (_playbackMode.value) {
            PlaybackMode.NORMAL -> {
                if (currentIndex < playlist.size - 1) {
                    playlist[currentIndex + 1]
                } else null
            }
            
            PlaybackMode.REPEAT_ONE -> {
                currentTrack
            }
            
            PlaybackMode.SHUFFLE -> {
                getNextShuffleTrack(playlist, currentIndex)
            }
        }
    }
    
    fun getPreviousTrack(
        currentTrack: Track?,
        playlist: List<Track>,
        currentIndex: Int
    ): Track? {
        if (playlist.isEmpty()) return null
        
        return when (_playbackMode.value) {
            PlaybackMode.NORMAL -> {
                if (currentIndex > 0) {
                    playlist[currentIndex - 1]
                } else null
            }
            
            PlaybackMode.REPEAT_ONE -> {
                currentTrack
            }
            
            PlaybackMode.SHUFFLE -> {
                getPreviousShuffleTrack(playlist, currentIndex)
            }
        }
    }
    
    
    
    private fun resetShuffle(playlistSize: Int = 0) {
        if (playlistSize > 0) {
            shuffleIndices = (0 until playlistSize).shuffled(Random.Default)
            currentShuffleIndex = 0
        } else {
            shuffleIndices = emptyList()
            currentShuffleIndex = -1
        }
    }
    
    private fun getNextShuffleTrack(playlist: List<Track>, currentIndex: Int): Track? {
        if (shuffleIndices.isEmpty()) {
            resetShuffle(playlist.size)
        }
        
        if (currentShuffleIndex < shuffleIndices.size - 1) {
            currentShuffleIndex += 1
            val nextIndex = shuffleIndices[currentShuffleIndex]
            return playlist.getOrNull(nextIndex)
        }
        
        return null
    }
    
    private fun getPreviousShuffleTrack(playlist: List<Track>, currentIndex: Int): Track? {
        if (shuffleIndices.isEmpty()) {
            resetShuffle(playlist.size)
        }
        
        if (currentShuffleIndex > 0) {
            currentShuffleIndex -= 1
            val previousIndex = shuffleIndices[currentShuffleIndex]
            return playlist.getOrNull(previousIndex)
        }
        
        return null
    }
}