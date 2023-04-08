package com.brunoponte.everythingdroid.helpers

import com.brunoponte.everythingdroid.domain.musicPlayer.model.Song

class Constants {

    companion object {
        const val SpeedRadarUrl = "https://services.arcgis.com/1dSrzEWVQn5kHHyK/arcgis/rest/services/MOB_RadaresPaineis/FeatureServer/0/"

        val DefaultSongs = listOf(
            Song(
                1,
                "T. Schürger",
                "SoundHelix Song 1",
                "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3",
            ),
            Song(
                2,
                "T. Schürger",
                "SoundHelix Song 10",
                "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-10.mp3"
            ),
            Song(
                3,
                "T. Schürger",
                "SoundHelix Song 13",
                "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-13.mp3"
            ),
            Song(
                4,
                "T. Schürger",
                "SoundHelix Song 15",
                "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-15.mp3"
            ),
        )
    }

}