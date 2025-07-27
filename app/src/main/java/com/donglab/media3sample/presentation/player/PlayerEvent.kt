package com.donglab.media3sample.presentation.player

import com.donglab.media3sample.domain.model.Track

sealed class PlayerEvent {
    data class TrackChanged(val track: Track?) : PlayerEvent()
    data class PlaybackStateChanged(val state: PlaybackState) : PlayerEvent()
    data class IsPlayingChanged(val isPlaying: Boolean) : PlayerEvent()
    data class PositionChanged(val position: Long) : PlayerEvent()
    data class DurationChanged(val duration: Long) : PlayerEvent()
    data class Error(val message: String, val cause: Throwable? = null) : PlayerEvent()
}