package com.donglab.media3sample.presentation.player.extensions.examples

import android.content.Context
import com.donglab.media3sample.presentation.player.ExoMediaPlayer
import com.donglab.media3sample.presentation.player.extensions.playlist.PlaylistMediaPlayer
import com.donglab.media3sample.presentation.player.extensions.multiaudio.MultiAudioMediaPlayer
import com.donglab.media3sample.presentation.player.extensions.playbackmode.PlaybackModeMediaPlayer
import com.donglab.media3sample.presentation.player.extensions.playbackmode.PlaybackMode
import com.donglab.media3sample.domain.model.Track

/**
 * Decorator 패턴 기반 MediaPlayer 사용 예시
 */
class DecoratorUsageExample(private val context: Context) {
    
    // 1. 기본 플레이어만 사용
    fun basicPlayerExample() {
        val player = ExoMediaPlayer(context)
        
        val track = Track("1", "Basic Track", "Artist", "Album", 180000, "uri")
        player.playTrack(track)
        player.setVolume(0.8f)
    }
    
    // 2. 플레이리스트 기능만 추가
    fun playlistOnlyExample() {
        val player = PlaylistMediaPlayer(ExoMediaPlayer(context))
        
        val tracks = listOf(
            Track("1", "Track 1", "Artist 1", "Album 1", 180000, "uri1"),
            Track("2", "Track 2", "Artist 2", "Album 2", 200000, "uri2"),
            Track("3", "Track 3", "Artist 3", "Album 3", 220000, "uri3")
        )
        
        // 플레이리스트에 트랙 추가
        player.addTracks(tracks)
        
        // 첫 번째 트랙 재생
        tracks.firstOrNull()?.let { player.playTrack(it) }
        
        // 다음/이전 트랙으로 이동
        player.playNext()
        player.playPrevious()
        
        // 플레이리스트 관리
        player.removeTrack("2")
        player.moveTrack(0, 1)
    }
    
    // 3. 다중 음원 기능만 추가
    fun multiAudioOnlyExample() {
        val player = MultiAudioMediaPlayer(ExoMediaPlayer(context))
        
        // 서브 플레이어들 추가
        player.addSubPlayer("background", ExoMediaPlayer(context))
        player.addSubPlayer("effect", ExoMediaPlayer(context))
        
        val mainTrack = Track("main", "Main Track", "Artist", "Album", 180000, "main_uri")
        val bgTrack = Track("bg", "Background", "BG Artist", "BG Album", 200000, "bg_uri")
        val effectTrack = Track("fx", "Sound Effect", "FX Artist", "FX Album", 5000, "fx_uri")
        
        // 각 플레이어에서 재생
        player.playTrack(mainTrack)  // 메인 플레이어
        player.playTrackOnPlayer("background", bgTrack)
        player.playTrackOnPlayer("effect", effectTrack)
        
        // 개별 볼륨 조절
        player.setVolume(1.0f)  // 메인
        player.setPlayerVolume("background", 0.3f)
        player.setPlayerVolume("effect", 0.8f)
        
        // 모든 플레이어 제어
        player.pauseAll()
        player.setVolumeForAll(0.5f)
    }
    
    // 4. 플레이리스트 + 재생모드 조합
    fun playlistWithModeExample() {
        // Decorator 체이닝
        val player = PlaybackModeMediaPlayer(
            PlaylistMediaPlayer(ExoMediaPlayer(context))
        )
        
        val tracks = listOf(
            Track("1", "Track 1", "Artist 1", "Album 1", 180000, "uri1"),
            Track("2", "Track 2", "Artist 2", "Album 2", 200000, "uri2"),
            Track("3", "Track 3", "Artist 3", "Album 3", 220000, "uri3")
        )
        
        // PlaylistMediaPlayer 기능
        val playlistPlayer = player as PlaylistMediaPlayer
        playlistPlayer.addTracks(tracks)
        
        // PlaybackModeMediaPlayer 기능
        player.setPlaybackMode(PlaybackMode.SHUFFLE)
        
        tracks.firstOrNull()?.let { player.playTrack(it) }
        
        // 재생 모드에 따른 다음 트랙
        val playlist = playlistPlayer.getCurrentPlaylist()
        val currentIndex = playlistPlayer.currentIndex.value
        val nextTrack = player.getNextTrack(playlist, currentIndex)
        nextTrack?.let { player.playTrack(it) }
    }
    
    // 5. 플레이리스트 + 다중음원 조합
    fun playlistWithMultiAudioExample() {
        val player = MultiAudioMediaPlayer(
            PlaylistMediaPlayer(ExoMediaPlayer(context))
        )
        
        // 서브 플레이어 추가
        player.addSubPlayer("harmony", ExoMediaPlayer(context))
        
        // PlaylistMediaPlayer 기능 접근
        val playlistPlayer = player as PlaylistMediaPlayer
        val tracks = listOf(
            Track("1", "Song 1", "Artist 1", "Album 1", 180000, "uri1"),
            Track("2", "Song 2", "Artist 2", "Album 2", 200000, "uri2")
        )
        playlistPlayer.addTracks(tracks)
        
        val harmonyTrack = Track("h1", "Harmony 1", "Harmony Artist", "Album", 180000, "harmony_uri")
        
        // 메인 플레이리스트 재생
        tracks.firstOrNull()?.let { player.playTrack(it) }
        
        // 하모니 재생
        player.playTrackOnPlayer("harmony", harmonyTrack)
        
        // 볼륨 조절
        player.setVolume(0.8f)  // 메인
        player.setPlayerVolume("harmony", 0.4f)  // 하모니
        
        // 플레이리스트 다음 곡
        playlistPlayer.playNext()
    }
    
    // 6. 모든 기능 조합 (3중 Decorator)
    fun fullDecoratorExample() {
        val player = MultiAudioMediaPlayer(
            PlaybackModeMediaPlayer(
                PlaylistMediaPlayer(ExoMediaPlayer(context))
            )
        )
        
        // 서브 플레이어 추가
        player.addSubPlayer("background", ExoMediaPlayer(context))
        
        // 각 Decorator 기능별 접근
        val playlistPlayer = player as PlaylistMediaPlayer
        val playbackModePlayer = player as PlaybackModeMediaPlayer
        
        val tracks = listOf(
            Track("1", "Song 1", "Artist 1", "Album 1", 180000, "uri1"),
            Track("2", "Song 2", "Artist 2", "Album 2", 200000, "uri2"),
            Track("3", "Song 3", "Artist 3", "Album 3", 220000, "uri3")
        )
        
        // 플레이리스트 설정
        playlistPlayer.addTracks(tracks)
        
        // 재생 모드 설정
        playbackModePlayer.setPlaybackMode(PlaybackMode.SHUFFLE)
        
        // 배경음악 설정  
        val bgTrack = Track("bg", "Background", "BG Artist", "Album", 200000, "bg_uri")
        player.playTrackOnPlayer("background", bgTrack)
        
        // 메인 트랙 재생
        tracks.firstOrNull()?.let { player.playTrack(it) }
        
        // 셔플 모드에서 다음 트랙
        val playlist = playlistPlayer.getCurrentPlaylist()
        val currentIndex = playlistPlayer.currentIndex.value
        val nextTrack = playbackModePlayer.getNextTrack(playlist, currentIndex)
        nextTrack?.let { player.playTrack(it) }
        
        // 볼륨 믹싱
        player.setVolume(0.8f)
        player.setPlayerVolume("background", 0.3f)
    }
}