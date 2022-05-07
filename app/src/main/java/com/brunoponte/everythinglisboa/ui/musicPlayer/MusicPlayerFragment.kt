package com.brunoponte.everythinglisboa.ui.musicPlayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import com.brunoponte.everythinglisboa.services.MusicPlayerService
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

        requireContext().registerReceiver(songStatusReceiver, IntentFilter(BROADCAST_RECEIVER_ID))

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
        if (MusicPlayerService.isRunning) {
            Intent().also { intent ->
                intent.action = MusicPlayerService.BROADCAST_RECEIVER_ID
                intent.putExtra(MusicPlayerService.SONG_RECEIVER, song)
                requireContext().sendBroadcast(intent)
            }
            return
        } else {
            startForegroundService(song)
        }

    }

    override fun onDownload(position: Int, song: Song) {
        viewModel.onDownload(song)
    }

    override fun onIndexReached(index: Int) {
        //
    }



    private fun startForegroundService(song: Song) {
        val serviceIntent = Intent(requireContext(), MusicPlayerService::class.java).apply {
            action = MusicPlayerService.START_SERVICE_ACTION_CODE
        }
        serviceIntent.putExtra(MusicPlayerService.SONG_RECEIVER, song)
        requireContext().startService(serviceIntent)
    }

    private fun stopForegroundService() {
        val serviceIntent = Intent(requireContext(), MusicPlayerService::class.java)
        requireContext().stopService(serviceIntent)
    }

    private val songStatusReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.getParcelableExtra<Song?>(MusicPlayerService.PLAY_ACTION_CODE)?.let { song ->
                viewModel.onPlay(song)
            }

            intent.getParcelableExtra<Song?>(MusicPlayerService.PAUSE_ACTION_CODE)?.let { song ->
                viewModel.onPause(song)
            }
        }
    }

    companion object {
        const val BROADCAST_RECEIVER_ID = "MusicPlayerFragmentBroadcastReceiver"
    }
}