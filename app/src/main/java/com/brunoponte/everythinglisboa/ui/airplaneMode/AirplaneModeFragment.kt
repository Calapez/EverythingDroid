package com.brunoponte.everythinglisboa.ui.airplaneMode

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.brunoponte.everythinglisboa.R
import com.brunoponte.everythinglisboa.databinding.FragmentAirplaneModeBinding
import com.brunoponte.everythinglisboa.domain.musicPlayer.model.Song
import com.brunoponte.everythinglisboa.services.MusicPlayerService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AirplaneModeFragment : Fragment() {

    private lateinit var binding: FragmentAirplaneModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAirplaneModeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireContext().registerReceiver(airplaneModeChangedReceiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
    }

    private val airplaneModeChangedReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.getBooleanExtra(AIRPLANE_MODE_STATE_EXTRA, false).let { airplaneModeEnabled ->
                Toast.makeText(
                    requireContext(),
                    if (airplaneModeEnabled) R.string.airplane_mode_enabled else R.string.airplane_mode_disabled,
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val AIRPLANE_MODE_STATE_EXTRA = "state"
    }
}