package com.donglab.media3sample.presentation.intent

import com.donglab.media3sample.domain.model.Track

sealed class MusicPlayerIntent {
    object LoadTracks : MusicPlayerIntent()
    data class PlayTrack(val track: Track) : MusicPlayerIntent()
    object PauseTrack : MusicPlayerIntent()
    object ResumeTrack : MusicPlayerIntent()
    object SkipToNext : MusicPlayerIntent()
    object SkipToPrevious : MusicPlayerIntent()
    data class SeekTo(val position: Long) : MusicPlayerIntent()
}