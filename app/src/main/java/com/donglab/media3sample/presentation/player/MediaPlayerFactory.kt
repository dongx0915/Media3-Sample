package com.donglab.media3sample.presentation.player

import android.content.Context

/**
 * MediaPlayer 팩토리 클래스
 * 다른 프로젝트에서 쉽게 사용할 수 있도록 하는 진입점
 */
object MediaPlayerFactory {
    
    /**
     * ExoPlayer 기반 MediaPlayer 생성
     */
    fun createExoPlayer(context: Context): MediaPlayer {
        return ExoMediaPlayer(context)
    }
    
    /**
     * 기본 설정으로 MediaPlayer 생성
     */
    fun createDefault(context: Context): MediaPlayer {
        return createExoPlayer(context)
    }
    
    /**
     * 사용자 정의 설정으로 MediaPlayer 생성
     */
    fun createWithConfig(
        context: Context,
        config: MediaPlayerConfig = MediaPlayerConfig()
    ): MediaPlayer {
        val player = createExoPlayer(context)
        
        // 설정 적용
        // 향후 config 옵션들을 적용할 수 있음
        
        return player
    }
}

/**
 * MediaPlayer 설정 클래스
 * 다양한 설정 옵션을 제공하여 재사용성을 높임
 */
data class MediaPlayerConfig(
    val enableBuffering: Boolean = true,
    val enableSeekBar: Boolean = true,
    val autoPlay: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val shuffleMode: Boolean = false,
    val defaultVolume: Float = 1.0f,
    val defaultSpeed: Float = 1.0f
)

enum class RepeatMode {
    OFF,
    ONE,
    ALL
}