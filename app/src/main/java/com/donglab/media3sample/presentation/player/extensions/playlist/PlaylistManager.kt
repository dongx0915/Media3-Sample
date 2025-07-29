package com.donglab.media3sample.presentation.player.extensions.playlist

import com.donglab.media3sample.domain.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlaylistManager {
    
    private val _playlist = MutableStateFlow<List<Track>>(emptyList())
    val playlist: StateFlow<List<Track>> = _playlist.asStateFlow()
    
    private val _currentIndex = MutableStateFlow(-1)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()
    
    val currentTrack: Track?
        get() = if (_currentIndex.value >= 0 && _currentIndex.value < _playlist.value.size) {
            _playlist.value[_currentIndex.value]
        } else null
    
    
    fun addTrack(track: Track, position: Int = -1) {
        val currentList = _playlist.value.toMutableList()
        val insertPosition = if (position == -1) currentList.size else position.coerceIn(0, currentList.size)
        currentList.add(insertPosition, track)
        _playlist.value = currentList
    }
    
    fun addTracks(tracks: List<Track>, position: Int = -1) {
        val currentList = _playlist.value.toMutableList()
        val insertPosition = if (position == -1) currentList.size else position.coerceIn(0, currentList.size)
        currentList.addAll(insertPosition, tracks)
        _playlist.value = currentList
    }
    
    fun removeTrack(trackId: String) {
        val currentList = _playlist.value.toMutableList()
        val removeIndex = currentList.indexOfFirst { it.id == trackId }
        if (removeIndex != -1) {
            currentList.removeAt(removeIndex)
            _playlist.value = currentList
            
            // 현재 인덱스 조정
            when {
                _currentIndex.value == removeIndex -> {
                    // 현재 재생 중인 트랙이 삭제된 경우
                    if (currentList.isEmpty()) {
                        _currentIndex.value = -1
                    } else if (_currentIndex.value >= currentList.size) {
                        _currentIndex.value = currentList.size - 1
                    }
                }
                _currentIndex.value > removeIndex -> {
                    _currentIndex.value = _currentIndex.value - 1
                }
            }
        }
    }
    
    fun moveTrack(fromIndex: Int, toIndex: Int) {
        val currentList = _playlist.value.toMutableList()
        if (fromIndex in currentList.indices && toIndex in currentList.indices) {
            val track = currentList.removeAt(fromIndex)
            currentList.add(toIndex, track)
            _playlist.value = currentList
            
            // 현재 인덱스 조정
            when {
                _currentIndex.value == fromIndex -> _currentIndex.value = toIndex
                fromIndex < _currentIndex.value && toIndex >= _currentIndex.value -> _currentIndex.value = _currentIndex.value - 1
                fromIndex > _currentIndex.value && toIndex <= _currentIndex.value -> _currentIndex.value = _currentIndex.value + 1
            }
        }
    }
    
    fun setCurrentIndex(index: Int) {
        if (index in -1 until _playlist.value.size) {
            _currentIndex.value = index
        }
    }
    
    fun setCurrentTrack(track: Track) {
        val index = _playlist.value.indexOfFirst { it.id == track.id }
        if (index != -1) {
            _currentIndex.value = index
        }
    }
    
    
    fun clear() {
        _playlist.value = emptyList()
        _currentIndex.value = -1
    }
    
    fun getCurrentPlaylist(): List<Track> = _playlist.value
    
}