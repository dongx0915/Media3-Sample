package com.donglab.media3sample.presentation.player

import com.donglab.media3sample.domain.model.Track
import kotlinx.coroutines.flow.Flow

/**
 * 재사용 가능한 미디어 플레이어 추상화 인터페이스
 * 다양한 미디어 플레이어 구현체(ExoPlayer, MediaPlayer 등)를 지원할 수 있도록 설계
 */
interface MediaPlayer {
    
    /**
     * 플레이어 이벤트를 구독할 수 있는 Flow
     */
    val playerEvents: Flow<PlayerEvent>
    
    /**
     * 현재 재생 위치 (밀리초)
     */
    val currentPosition: Long
    
    /**
     * 현재 트랙의 총 재생 시간 (밀리초)
     */
    val duration: Long
    
    /**
     * 현재 재생 상태
     */
    val playbackState: PlaybackState
    
    /**
     * 현재 재생 중인지 여부
     */
    val isPlaying: Boolean
    
    /**
     * 현재 재생 중인 트랙
     */
    val currentTrack: Track?
    
    /**
     * 트랙을 준비하고 재생
     */
    fun playTrack(track: Track)
    
    /**
     * 재생 일시정지
     */
    fun pause()
    
    /**
     * 재생 재개
     */
    fun resume()
    
    /**
     * 재생 중지
     */
    fun stop()
    
    /**
     * 특정 위치로 이동 (밀리초)
     */
    fun seekTo(position: Long)
    
    
    /**
     * 볼륨 설정 (0.0f ~ 1.0f)
     */
    fun setVolume(volume: Float)
    
    /**
     * 재생 속도 설정 (0.5f ~ 2.0f)
     */
    fun setPlaybackSpeed(speed: Float)
    
    /**
     * 리소스 해제
     */
    fun release()
}