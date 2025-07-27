package com.donglab.media3sample.domain.usecase

import com.donglab.media3sample.data.repository.MusicRepository
import com.donglab.media3sample.domain.model.Track

class SearchTracksUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(query: String): List<Track> {
        return musicRepository.searchTracks(query).map { dataTrack ->
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