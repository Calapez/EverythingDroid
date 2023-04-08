package com.brunoponte.everythingdroid.ui.musicPlayer.list_adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.brunoponte.everythingdroid.R
import com.brunoponte.everythingdroid.databinding.MusicPlayerListItemBinding
import com.brunoponte.everythingdroid.domain.musicPlayer.model.Song
import com.squareup.picasso.Picasso

class MusicPlayerViewHolder(
    private val binding: MusicPlayerListItemBinding,
    private val interaction: MusicPlayerListInteraction
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(song: Song, position: Int) {
        binding.apply {
            if (song.albumCoverUrl != null) {
                Picasso.get().load(song.albumCoverUrl).into(imageSong)
            } else {
                imageSong.setImageResource(R.drawable.ic_no_album_cover)
            }

            imagePlayPause.setImageResource(
                if (song.isPlaying) R.drawable.ic_pause
                else R.drawable.ic_play)

            imageDownload.setImageResource(
                if (song.isDownloading) R.drawable.ic_downloading
                else R.drawable.ic_download)

            imageDownload.visibility =
                if (song.savedPath.isNullOrEmpty()) View.VISIBLE
                else View.INVISIBLE

            textTitle.text = song.title

            textArtist.text = song.artist

            imagePlayPause.setOnClickListener {
                interaction.onPlayPause(position, song)
            }

            imageDownload.setOnClickListener {
                interaction.onDownload(position, song)
            }
        }
    }
}