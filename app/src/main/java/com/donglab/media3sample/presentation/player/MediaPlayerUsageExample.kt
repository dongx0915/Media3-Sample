package com.donglab.media3sample.presentation.player

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.donglab.media3sample.domain.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 다른 프로젝트에서 MediaPlayer를 사용하는 예제
 * 이 파일을 참고하여 자신의 프로젝트에 적용할 수 있습니다.
 */

// ========================================
// 1. 기본 사용법
// ========================================

class SimplePlayerViewModel(context: Context) : ViewModel() {
    
    private val mediaPlayer = MediaPlayerFactory.createDefault(context)
    
    fun playTrack(track: Track) {
        viewModelScope.launch {
            mediaPlayer.playTrack(track)
        }
    }
    
    fun pause() {
        viewModelScope.launch {
            mediaPlayer.pause()
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            mediaPlayer.release()
        }
    }
}

// ========================================
// 2. 고급 사용법 (이벤트 구독)
// ========================================

class AdvancedPlayerViewModel(context: Context) : ViewModel() {
    
    private val mediaPlayer = MediaPlayerFactory.createWithConfig(
        context = context,
        config = MediaPlayerConfig(
            autoPlay = true,
            defaultVolume = 0.8f,
            repeatMode = RepeatMode.ALL
        )
    )
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()
    
    private val _position = MutableStateFlow(0L)
    val position: StateFlow<Long> = _position.asStateFlow()
    
    init {
        observePlayerEvents()
    }
    
    private fun observePlayerEvents() {
        viewModelScope.launch {
            mediaPlayer.playerEvents.collect { event ->
                when (event) {
                    is PlayerEvent.IsPlayingChanged -> {
                        _isPlaying.value = event.isPlaying
                    }
                    is PlayerEvent.TrackChanged -> {
                        _currentTrack.value = event.track
                    }
                    is PlayerEvent.PositionChanged -> {
                        _position.value = event.position
                    }
                    is PlayerEvent.Error -> {
                        // 에러 처리
                        handleError(event.message, event.cause)
                    }
                    else -> {
                        // 다른 이벤트 처리
                    }
                }
            }
        }
    }
    
    fun setPlaylist(tracks: List<Track>) {
        viewModelScope.launch {
            mediaPlayer.setPlaylist(tracks)
        }
    }
    
    fun seekTo(position: Long) {
        viewModelScope.launch {
            mediaPlayer.seekTo(position)
        }
    }
    
    fun setVolume(volume: Float) {
        viewModelScope.launch {
            mediaPlayer.setVolume(volume)
        }
    }
    
    private fun handleError(message: String, cause: Throwable?) {
        // 에러 로깅, 사용자에게 알림 등
        println("Player Error: $message")
        cause?.printStackTrace()
    }
    
    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            mediaPlayer.release()
        }
    }
}

// ========================================
// 3. 커스텀 MediaPlayer 구현 예제
// ========================================

/**
 * 다른 플레이어 라이브러리를 사용하고 싶다면 MediaPlayer 인터페이스를 구현하세요
 */
/*
class CustomMediaPlayer(context: Context) : MediaPlayer {
    
    // 사용자 정의 플레이어 라이브러리 초기화
    private val customPlayer = YourCustomPlayerLibrary(context)
    
    override val playerEvents: Flow<PlayerEvent> = flow {
        // 커스텀 플레이어의 이벤트를 PlayerEvent로 변환
    }
    
    override suspend fun playTrack(track: Track) {
        // 커스텀 플레이어로 트랙 재생
        customPlayer.play(track.uri)
    }
    
    // 다른 메서드들도 동일하게 구현...
}
*/

// ========================================
// 4. Compose UI에서 사용하는 예제
// ========================================

/*
@Composable
fun PlayerScreen() {
    val context = LocalContext.current
    val viewModel: AdvancedPlayerViewModel = viewModel { AdvancedPlayerViewModel(context) }
    
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentTrack by viewModel.currentTrack.collectAsState()
    val position by viewModel.position.collectAsState()
    
    Column {
        currentTrack?.let { track ->
            Text(text = track.title)
            Text(text = track.artist)
        }
        
        Button(
            onClick = {
                if (isPlaying) {
                    viewModel.pause()
                } else {
                    viewModel.resume()
                }
            }
        ) {
            Text(if (isPlaying) "Pause" else "Play")
        }
        
        Slider(
            value = position.toFloat(),
            onValueChange = { viewModel.seekTo(it.toLong()) },
            valueRange = 0f..viewModel.duration.toFloat()
        )
    }
}
*/