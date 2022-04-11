package com.brunoponte.everythinglisboa.ui.musicPlayer.list_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.brunoponte.everythinglisboa.databinding.MusicPlayerListItemBinding
import com.brunoponte.everythinglisboa.domain.musicPlayer.model.Song


interface MusicPlayerListInteraction {
    fun onPlayPause(position: Int, song: Song)

    fun onDownload(position: Int, song: Song)

    fun onIndexReached(index: Int)
}

class MusicPlayerListAdapter(
    private val interaction: MusicPlayerListInteraction
) : ListAdapter<Song, MusicPlayerViewHolder>(MusicPlayerListAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicPlayerViewHolder {
        val itemBinding = MusicPlayerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicPlayerViewHolder(itemBinding, interaction)
    }

    override fun onBindViewHolder(holder: MusicPlayerViewHolder, position: Int) {
        holder.bind(getItem(position), position)
        interaction.onIndexReached(position)
    }

    private companion object : DiffUtil.ItemCallback<Song>() {

        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }
    }
}