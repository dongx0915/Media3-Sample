package com.donglab.media3sample.presentation.state

import com.donglab.media3sample.domain.model.Track

data class MusicPlayerState(
    val tracks: List<Track> = emptyList(),
    val currentTrack: Track? = null,
    val isPlaying: Boolean = false,
    val playbackPosition: Long = 0L,
    val duration: Long = 0L,
    val isLoading: Boolean = false,
    val error: String? = null
)