package com.donglab.media3sample.presentation.player.extensions.playbackmode

import com.donglab.media3sample.domain.model.Track
import kotlinx.coroutines.flow.StateFlow

interface PlaybackModeComponent {
    val playbackMode: StateFlow<PlaybackMode>
    
    fun setPlaybackMode(mode: PlaybackMode)
    fun getNextTrack(currentTrack: Track?, playlist: List<Track>, currentIndex: Int): Track?
    fun getPreviousTrack(currentTrack: Track?, playlist: List<Track>, currentIndex: Int): Track?
}

class DefaultPlaybackModeComponent : PlaybackModeComponent {
    
    private val playbackModeManager = PlaybackModeManager()
    
    override val playbackMode: StateFlow<PlaybackMode> = playbackModeManager.playbackMode
    
    override fun setPlaybackMode(mode: PlaybackMode) {
        playbackModeManager.setPlaybackMode(mode)
    }
    
    override fun getNextTrack(currentTrack: Track?, playlist: List<Track>, currentIndex: Int): Track? {
        return playbackModeManager.getNextTrack(currentTrack, playlist, currentIndex)
    }
    
    override fun getPreviousTrack(currentTrack: Track?, playlist: List<Track>, currentIndex: Int): Track? {
        return playbackModeManager.getPreviousTrack(currentTrack, playlist, currentIndex)
    }
}