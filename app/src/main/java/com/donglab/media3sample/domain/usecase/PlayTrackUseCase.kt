package com.donglab.media3sample.domain.usecase

import com.donglab.media3sample.data.repository.MusicRepository
import com.donglab.media3sample.domain.model.Track

class PlayTrackUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(track: Track) {
        val dataTrack = com.donglab.media3sample.data.model.Track(
            id = track.id,
            title = track.title,
            artist = track.artist,
            album = track.album,
            duration = track.duration,
            uri = track.uri,
            albumArtUri = track.albumArtUri
        )
        musicRepository.playTrack(dataTrack)
    }
}