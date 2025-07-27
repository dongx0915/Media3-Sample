package com.donglab.media3sample.data.repository

import com.donglab.media3sample.data.model.Track
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    suspend fun getTracks(): List<Track>
    fun getCurrentTrack(): Flow<Track?>
    suspend fun playTrack(track: Track)
    suspend fun pause()
    suspend fun resume()
    suspend fun stop()
    suspend fun seekTo(position: Long)
    suspend fun skipToNext()
    suspend fun skipToPrevious()
    fun getPlaybackPosition(): Flow<Long>
    fun isPlaying(): Flow<Boolean>
    fun getDuration(): Flow<Long>
}