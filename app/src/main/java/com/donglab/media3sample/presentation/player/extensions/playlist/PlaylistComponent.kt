package com.donglab.media3sample.presentation.player.extensions.playlist

import com.donglab.media3sample.domain.model.Track
import kotlinx.coroutines.flow.StateFlow

interface PlaylistComponent {
    val playlist: StateFlow<List<Track>>
    val currentIndex: StateFlow<Int>
    val currentTrack: Track?
    
    fun addTrack(track: Track, position: Int = -1)
    fun addTracks(tracks: List<Track>, position: Int = -1)
    fun removeTrack(trackId: String)
    fun moveTrack(fromIndex: Int, toIndex: Int)
    fun setCurrentIndex(index: Int)
    fun setCurrentTrack(track: Track)
    fun clear()
    fun getCurrentPlaylist(): List<Track>
}

class DefaultPlaylistComponent : PlaylistComponent {
    
    private val playlistManager = PlaylistManager()
    
    override val playlist: StateFlow<List<Track>> = playlistManager.playlist
    override val currentIndex: StateFlow<Int> = playlistManager.currentIndex
    override val currentTrack: Track? get() = playlistManager.currentTrack
    
    override fun addTrack(track: Track, position: Int) {
        playlistManager.addTrack(track, position)
    }
    
    override fun addTracks(tracks: List<Track>, position: Int) {
        playlistManager.addTracks(tracks, position)
    }
    
    override fun removeTrack(trackId: String) {
        playlistManager.removeTrack(trackId)
    }
    
    override fun moveTrack(fromIndex: Int, toIndex: Int) {
        playlistManager.moveTrack(fromIndex, toIndex)
    }
    
    override fun setCurrentIndex(index: Int) {
        playlistManager.setCurrentIndex(index)
    }
    
    override fun setCurrentTrack(track: Track) {
        playlistManager.setCurrentTrack(track)
    }
    
    override fun clear() {
        playlistManager.clear()
    }
    
    override fun getCurrentPlaylist(): List<Track> {
        return playlistManager.getCurrentPlaylist()
    }
}