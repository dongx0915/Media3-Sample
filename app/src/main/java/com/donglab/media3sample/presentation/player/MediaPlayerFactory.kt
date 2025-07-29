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
}