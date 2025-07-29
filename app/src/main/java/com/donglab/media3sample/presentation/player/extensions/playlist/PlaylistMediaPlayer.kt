package com.donglab.media3sample.presentation.player.extensions.playlist

import com.donglab.media3sample.presentation.player.MediaPlayer
import com.donglab.media3sample.presentation.player.PlaybackState
import com.donglab.media3sample.presentation.player.PlayerEvent
import com.donglab.media3sample.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * 플레이리스트 기능을 추가하는 MediaPlayer Decorator
 */
class PlaylistMediaPlayer(
    private val mediaPlayer: MediaPlayer,
    private val playlistComponent: PlaylistComponent = DefaultPlaylistComponent()
) : MediaPlayer {
    
    // 기본 MediaPlayer 기능 위임
    override val playbackState: PlaybackState get() = mediaPlayer.playbackState
    override val playerEvents: Flow<PlayerEvent> get() = mediaPlayer.playerEvents
    override val currentPosition: Long get() = mediaPlayer.currentPosition
    override val duration: Long get() = mediaPlayer.duration
    override val isPlaying: Boolean get() = mediaPlayer.isPlaying
    override val currentTrack: Track? get() = mediaPlayer.currentTrack
    
    override fun playTrack(track: Track) {
        mediaPlayer.playTrack(track)
        playlistComponent.setCurrentTrack(track)
    }
    
    override fun pause() = mediaPlayer.pause()
    override fun resume() = mediaPlayer.resume()
    override fun stop() = mediaPlayer.stop()
    override fun seekTo(millis: Long) = mediaPlayer.seekTo(millis)
    override fun setVolume(volume: Float) = mediaPlayer.setVolume(volume)
    override fun setPlaybackSpeed(speed: Float) = mediaPlayer.setPlaybackSpeed(speed)
    override fun release() = mediaPlayer.release()
    
    // 플레이리스트 전용 기능들
    val playlist: StateFlow<List<Track>> = playlistComponent.playlist
    val currentIndex: StateFlow<Int> = playlistComponent.currentIndex
    
    fun addTrack(track: Track, position: Int = -1) {
        playlistComponent.addTrack(track, position)
    }
    
    fun addTracks(tracks: List<Track>, position: Int = -1) {
        playlistComponent.addTracks(tracks, position)
    }
    
    fun removeTrack(trackId: String) {
        playlistComponent.removeTrack(trackId)
    }
    
    fun moveTrack(fromIndex: Int, toIndex: Int) {
        playlistComponent.moveTrack(fromIndex, toIndex)
    }
    
    fun clear() {
        playlistComponent.clear()
    }
    
    fun getCurrentPlaylist(): List<Track> {
        return playlistComponent.getCurrentPlaylist()
    }
    
    fun getNextTrack(): Track? {
        val playlist = getCurrentPlaylist()
        val currentIndex = this.currentIndex.value
        return if (currentIndex < playlist.size - 1) {
            playlist[currentIndex + 1]
        } else null
    }
    
    fun getPreviousTrack(): Track? {
        val playlist = getCurrentPlaylist()
        val currentIndex = this.currentIndex.value
        return if (currentIndex > 0) {
            playlist[currentIndex - 1]
        } else null
    }
    
    fun playNext() {
        getNextTrack()?.let { playTrack(it) }
    }
    
    fun playPrevious() {
        getPreviousTrack()?.let { playTrack(it) }
    }
}