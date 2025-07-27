package com.donglab.media3sample.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.donglab.media3sample.data.repository.MusicRepository
import com.donglab.media3sample.data.repository.MusicRepositoryImpl
import com.donglab.media3sample.domain.usecase.*
import com.donglab.media3sample.presentation.intent.MusicPlayerIntent
import com.donglab.media3sample.presentation.state.MusicPlayerState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MusicPlayerViewModel(context: Context) : ViewModel() {

    private val musicRepository: MusicRepository = MusicRepositoryImpl(context)
    private val getTracksUseCase = GetTracksUseCase(musicRepository)
    private val playTrackUseCase = PlayTrackUseCase(musicRepository)
    private val pauseTrackUseCase = PauseTrackUseCase(musicRepository)
    private val resumeTrackUseCase = ResumeTrackUseCase(musicRepository)
    private val getCurrentTrackUseCase = GetCurrentTrackUseCase(musicRepository)

    private val _state = MutableStateFlow(MusicPlayerState())
    val state: StateFlow<MusicPlayerState> = _state.asStateFlow()

    init {
        observeCurrentTrack()
        observePlaybackState()
        observePlaybackPosition()
        observeDuration()
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

    private fun loadTracks() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                val tracks = getTracksUseCase()
                _state.value = _state.value.copy(
                    tracks = tracks,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun playTrack(track: com.donglab.media3sample.domain.model.Track) {
        viewModelScope.launch {
            try {
                playTrackUseCase(track)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    private fun pauseTrack() {
        viewModelScope.launch {
            try {
                pauseTrackUseCase()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    private fun resumeTrack() {
        viewModelScope.launch {
            try {
                resumeTrackUseCase()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    private fun skipToNext() {
        viewModelScope.launch {
            try {
                musicRepository.skipToNext()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    private fun skipToPrevious() {
        viewModelScope.launch {
            try {
                musicRepository.skipToPrevious()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    private fun seekTo(position: Long) {
        viewModelScope.launch {
            try {
                musicRepository.seekTo(position)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    private fun observeCurrentTrack() {
        viewModelScope.launch {
            getCurrentTrackUseCase().collect { track ->
                _state.value = _state.value.copy(currentTrack = track)
            }
        }
    }

    private fun observePlaybackState() {
        viewModelScope.launch {
            musicRepository.isPlaying().collect { isPlaying ->
                _state.value = _state.value.copy(isPlaying = isPlaying)
            }
        }
    }

    private fun observePlaybackPosition() {
        viewModelScope.launch {
            musicRepository.getPlaybackPosition().collect { position ->
                _state.value = _state.value.copy(playbackPosition = position)
            }
        }
    }

    private fun observeDuration() {
        viewModelScope.launch {
            musicRepository.getDuration().collect { duration ->
                _state.value = _state.value.copy(duration = duration)
            }
        }
    }
}