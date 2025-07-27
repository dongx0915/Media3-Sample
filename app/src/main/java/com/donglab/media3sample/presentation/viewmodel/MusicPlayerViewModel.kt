package com.donglab.media3sample.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.donglab.media3sample.data.repository.MusicRepositoryImpl
import com.donglab.media3sample.domain.usecase.GetTracksUseCase
import com.donglab.media3sample.domain.usecase.SearchTracksUseCase
import com.donglab.media3sample.presentation.intent.MusicPlayerIntent
import com.donglab.media3sample.presentation.player.ExoMediaPlayer
import com.donglab.media3sample.presentation.player.MediaPlayer
import com.donglab.media3sample.presentation.player.MediaPlayerFactory
import com.donglab.media3sample.presentation.player.PlayerEvent
import com.donglab.media3sample.presentation.state.MusicPlayerState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * 재사용 가능한 구조로 설계된 MusicPlayerViewModel
 * MediaPlayer와 Repository를 분리하여 사용
 */
class MusicPlayerViewModel(context: Context) : ViewModel() {

    // Repository (Data Layer)
    private val musicRepository = MusicRepositoryImpl()
    private val getTracksUseCase = GetTracksUseCase(musicRepository)
    private val searchTracksUseCase = SearchTracksUseCase(musicRepository)

    // MediaPlayer (Presentation Layer)
    private val mediaPlayer: MediaPlayer = MediaPlayerFactory.createDefault(context)

    private val _state = MutableStateFlow(MusicPlayerState())
    val state: StateFlow<MusicPlayerState> = _state.asStateFlow()

    init {
        observePlayerEvents()
        observePositionUpdates()
    }

    fun handleIntent(intent: MusicPlayerIntent) {
        when (intent) {
            is MusicPlayerIntent.LoadTracks -> loadTracks()
            is MusicPlayerIntent.PlayTrack -> playTrack(intent.track)
            is MusicPlayerIntent.PauseTrack -> pauseTrack()
            is MusicPlayerIntent.ResumeTrack -> resumeTrack()
            is MusicPlayerIntent.SkipToNext -> skipToNext()
            is MusicPlayerIntent.SkipToPrevious -> skipToPrevious()
            is MusicPlayerIntent.SeekTo -> seekTo(intent.position)
        }
    }

    private fun observePlayerEvents() {
        viewModelScope.launch {
            mediaPlayer.playerEvents.collect { event ->
                when (event) {
                    is PlayerEvent.TrackChanged -> {
                        _state.value = _state.value.copy(currentTrack = event.track)
                    }
                    is PlayerEvent.IsPlayingChanged -> {
                        _state.value = _state.value.copy(isPlaying = event.isPlaying)
                    }
                    is PlayerEvent.DurationChanged -> {
                        _state.value = _state.value.copy(duration = event.duration)
                    }
                    is PlayerEvent.PositionChanged -> {
                        _state.value = _state.value.copy(playbackPosition = event.position)
                    }
                    is PlayerEvent.PlaybackStateChanged -> {
                        // 필요에 따라 PlaybackState를 State에 추가할 수 있음
                    }
                    is PlayerEvent.Error -> {
                        _state.value = _state.value.copy(
                            error = event.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun observePositionUpdates() {
        viewModelScope.launch {
            // ExoMediaPlayer의 position 업데이트 구독
            if (mediaPlayer is ExoMediaPlayer) {
                mediaPlayer.getPositionUpdates().collect { position ->
                    _state.value = _state.value.copy(playbackPosition = position)
                }
            }
        }
    }

    private fun loadTracks() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)
                val tracks = getTracksUseCase()
                _state.value = _state.value.copy(
                    tracks = tracks,
                    isLoading = false
                )
                
                // 플레이리스트 설정
                if (tracks.isNotEmpty()) {
                    mediaPlayer.setPlaylist(tracks)
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load tracks"
                )
            }
        }
    }

    private fun playTrack(track: com.donglab.media3sample.domain.model.Track) {
        viewModelScope.launch {
            try {
                mediaPlayer.playTrack(track)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Failed to play track: ${e.message}"
                )
            }
        }
    }

    private fun pauseTrack() {
        viewModelScope.launch {
            try {
                mediaPlayer.pause()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Failed to pause: ${e.message}"
                )
            }
        }
    }

    private fun resumeTrack() {
        viewModelScope.launch {
            try {
                mediaPlayer.resume()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Failed to resume: ${e.message}"
                )
            }
        }
    }

    private fun skipToNext() {
        viewModelScope.launch {
            try {
                mediaPlayer.skipToNext()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Failed to skip to next: ${e.message}"
                )
            }
        }
    }

    private fun skipToPrevious() {
        viewModelScope.launch {
            try {
                mediaPlayer.skipToPrevious()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Failed to skip to previous: ${e.message}"
                )
            }
        }
    }

    private fun seekTo(position: Long) {
        viewModelScope.launch {
            try {
                mediaPlayer.seekTo(position)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Failed to seek: ${e.message}"
                )
            }
        }
    }

    fun searchTracks(query: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)
                val tracks = searchTracksUseCase(query)
                _state.value = _state.value.copy(
                    tracks = tracks,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Search failed: ${e.message}"
                )
            }
        }
    }

    fun setVolume(volume: Float) {
        viewModelScope.launch {
            mediaPlayer.setVolume(volume)
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        viewModelScope.launch {
            mediaPlayer.setPlaybackSpeed(speed)
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            mediaPlayer.release()
        }
    }
}