package com.brunoponte.everythinglisboa.ui.musicPlayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brunoponte.everythinglisboa.databinding.FragmentMusicPlayerBinding
import com.brunoponte.everythinglisboa.domain.musicPlayer.model.Song
import com.brunoponte.everythinglisboa.ui.musicPlayer.list_adapter.MusicPlayerListAdapter
import com.brunoponte.everythinglisboa.ui.musicPlayer.list_adapter.MusicPlayerListInteraction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicPlayerFragment : Fragment(), MusicPlayerListInteraction {

    private lateinit var binding: FragmentMusicPlayerBinding

    private val viewModel: MusicPlayerViewModel by viewModels()
    private val listAdapter = MusicPlayerListAdapter(this).apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy
            .PREVENT_WHEN_EMPTY
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMusicPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewSongs.let { recyclerView ->
            recyclerView.layoutManager = LinearLayoutManager(context).also {
                it.orientation = LinearLayoutManager.VERTICAL
            }
            recyclerView.adapter = listAdapter
        }

        setupViewModelObservers()
    }

    private fun setupViewModelObservers() {
        viewModel.songs.observe(viewLifecycleOwner) { songs ->
            listAdapter.submitList(songs)
        }
    }

    override fun onPlayPause(position: Int, song: Song) {
        viewModel.onPlayPause(song)
    }

    override fun onDownload(position: Int, song: Song) {
        viewModel.onDownload(song)
    }

    override fun onIndexReached(index: Int) {
        //
    }
}