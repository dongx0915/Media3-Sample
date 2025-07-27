package com.donglab.media3sample.domain.usecase

import com.donglab.media3sample.data.repository.MusicRepository

class ResumeTrackUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke() {
        musicRepository.resume()
    }
}