package com.donglab.media3sample.domain.usecase

import com.donglab.media3sample.data.repository.MusicRepository
import com.donglab.media3sample.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCurrentTrackUseCase(
    private val musicRepository: MusicRepository
) {
    operator fun invoke(): Flow<Track?> {
        return musicRepository.getCurrentTrack().map { dataTrack ->
            dataTrack?.let {
                Track(
                    id = it.id,
                    title = it.title,
                    artist = it.artist,
                    album = it.album,
                    duration = it.duration,
                    uri = it.uri,
                    albumArtUri = it.albumArtUri
                )
            }
        }
    }
}