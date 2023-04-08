package com.brunoponte.everythingdroid.ui.servicesPlayground

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
import com.brunoponte.everythingdroid.databinding.FragmentServicesPlaygroundBinding
import com.brunoponte.everythingdroid.ui.musicPlayer.MusicPlayerFragment


class ServicesPlaygroundFragment : Fragment() {

    private lateinit var binding: FragmentServicesPlaygroundBinding

    private val viewModel: ServicesPlaygroundViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServicesPlaygroundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireContext().registerReceiver(songStatusReceiver, IntentFilter(CounterBackgroundService.BROADCAST_TRANSMITTER_ID))

        binding.serviceButton.setOnClickListener {
            activity?.startService(Intent(activity, CounterBackgroundService::class.java))
        }
    }

    private val songStatusReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.getIntExtra(CounterBackgroundService.BROADCAST_COUNTER_ID, -1).let { counter ->
                binding.serviceText.text = counter.toString()
            }
        }
    }



}