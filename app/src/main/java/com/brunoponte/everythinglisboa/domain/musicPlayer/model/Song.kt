package com.brunoponte.everythinglisboa.domain.musicPlayer.model

data class Song (
    val id: Long,
    val artist: String,
    val title: String,
    val url: String,
    val albumCoverUrl: String? = null,
    var savedPath: String? = null,
    var isPlaying: Boolean = false,
    var isDownloading: Boolean = false
)