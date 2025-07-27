package com.donglab.media3sample.domain.usecase

import com.donglab.media3sample.data.repository.MusicRepository
import com.donglab.media3sample.domain.model.Track

class GetTracksUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(): List<Track> {
        return musicRepository.getTracks().map { dataTrack ->
            Track(
                id = dataTrack.id,
                title = dataTrack.title,
                artist = dataTrack.artist,
                album = dataTrack.album,
                duration = dataTrack.duration,
                uri = dataTrack.uri,
                albumArtUri = dataTrack.albumArtUri
            )
        }
    }
}