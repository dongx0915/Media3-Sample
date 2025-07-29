package com.donglab.media3sample.presentation.player

import com.donglab.media3sample.domain.model.Track
import kotlinx.coroutines.flow.Flow

/**
 * 재사용 가능한 미디어 플레이어 추상화 인터페이스
 * 다양한 미디어 플레이어 구현체(ExoPlayer, MediaPlayer 등)를 지원할 수 있도록 설계
 */
interface MediaPlayer {

    // 현재 재생 생태
    val playbackState: PlaybackState

    val playerEvents: Flow<PlayerEvent>

    val currentPosition: Long

    val duration: Long

    val isPlaying: Boolean

    val currentTrack: Track?

    fun playTrack(track: Track)

    fun pause()

    fun resume()

    fun stop()

    fun seekTo(millis: Long)

    fun setVolume(volume: Float)

    fun setPlaybackSpeed(speed: Float)

    fun release()
}