package com.brunoponte.everythingdroid.domain.musicPlayer.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song (
    val id: Long,
    val artist: String,
    val title: String,
    val url: String,
    val albumCoverUrl: String? = null,
    var savedPath: String? = null,
    var isPlaying: Boolean = false,
    var isDownloading: Boolean = false
) : Parcelable