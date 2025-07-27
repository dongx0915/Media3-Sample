package com.donglab.media3sample.domain.usecase

import com.donglab.media3sample.data.repository.MusicRepository

class PauseTrackUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke() {
        musicRepository.pause()
    }
}