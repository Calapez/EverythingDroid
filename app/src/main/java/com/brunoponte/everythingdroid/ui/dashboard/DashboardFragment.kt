package com.brunoponte.everythingdroid.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brunoponte.everythingdroid.R
import com.brunoponte.everythingdroid.databinding.FragmentDashboardBinding
import com.brunoponte.everythingdroid.enums.DashboardOption
import com.brunoponte.everythingdroid.ui.dashboard.list_adapter.DashboardListAdapter
import com.brunoponte.everythingdroid.ui.dashboard.list_adapter.DashboardListInteraction
import com.google.ar.core.ArCoreApk
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment(), DashboardListInteraction {

    private lateinit var binding: FragmentDashboardBinding

    private val viewModel: DashboardViewModel by viewModels()
    private val listAdapter = DashboardListAdapter(this).apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy
            .PREVENT_WHEN_EMPTY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.let { recyclerView ->
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.adapter = listAdapter
        }

        setupViewModelObservers()
        maybeEnableArButton()
    }

    private fun setupViewModelObservers() {
        viewModel.features.observe(viewLifecycleOwner) { speedRadars ->
            listAdapter.submitList(speedRadars)
        }
    }

    override fun onClick(position: Int, dashboardOption: DashboardOption) {
        when (dashboardOption) {
            DashboardOption.CAMERA -> findNavController().navigate(R.id.action_dashboardFragment_to_cameraFragment)
            DashboardOption.RADARS -> findNavController().navigate(R.id.action_dashboardFragment_to_speedRadarFragment)
            DashboardOption.START_FOREGROUND_SERVICE -> findNavController().navigate(R.id.action_dashboardFragment_to_musicPlayerFragment)
            DashboardOption.DETECT_AIRPLANE_MODE -> findNavController().navigate(R.id.action_dashboardFragment_to_airplaneModeFragment)
            DashboardOption.APP_CHOOSER -> findNavController().navigate(R.id.action_dashboardFragment_to_appChooserFragment)
            DashboardOption.GITHUB_REPOS -> findNavController().navigate(R.id.action_dashboardFragment_to_coroutinesFragment)
            DashboardOption.LISBOA_ABERTA -> findNavController().navigate(R.id.action_dashboardFragment_to_lisboaAbertaFragment)
            else -> { }
        }
    }

    fun maybeEnableArButton() {
        val availability = ArCoreApk.getInstance().checkAvailability(activity)
        if (availability.isTransient) {
            // Continue to query availability at 5Hz while compatibility is checked in the background.
            Handler().postDelayed({
                maybeEnableArButton()
            }, 200)
        }
        if (availability.isSupported) {
            /*
            visibility = View.VISIBLE
            mArButton.isEnabled = true
            */
        } else { // The device is unsupported or unknown.
            /*
            mArButton.visibility = View.INVISIBLE
            mArButton.isEnabled = false
             */
        }
    }

}