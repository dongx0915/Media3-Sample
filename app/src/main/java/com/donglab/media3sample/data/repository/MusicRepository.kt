package com.donglab.media3sample.data.repository

import com.donglab.media3sample.data.model.Track

interface MusicRepository {
    suspend fun getTracks(): List<Track>
    suspend fun getTrackById(id: String): Track?
    suspend fun searchTracks(query: String): List<Track>
}