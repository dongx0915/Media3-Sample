package com.donglab.media3sample.presentation.player


sealed class PlayerEvent {
    data class PlaybackStateChanged(val state: PlaybackState) : PlayerEvent()
    data class IsPlayingChanged(val isPlaying: Boolean) : PlayerEvent()
    data class Error(val message: String, val cause: Throwable? = null) : PlayerEvent()
}