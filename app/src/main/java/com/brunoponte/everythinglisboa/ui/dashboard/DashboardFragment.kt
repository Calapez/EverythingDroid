package com.brunoponte.everythinglisboa.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brunoponte.everythinglisboa.R
import com.brunoponte.everythinglisboa.databinding.FragmentDashboardBinding
import com.brunoponte.everythinglisboa.enums.DashboardOption
import com.brunoponte.everythinglisboa.ui.counterService.CounterService
import com.brunoponte.everythinglisboa.ui.dashboard.list_adapter.DashboardListAdapter
import com.brunoponte.everythinglisboa.ui.dashboard.list_adapter.DashboardListInteraction
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
            DashboardOption.START_FOREGROUND_SERVICE -> startForegroundService()
            DashboardOption.STOP_FOREGROUND_SERVICE -> stopForegroundService()
            else -> { }
        }
    }

    override fun onIndexReached(index: Int) {
        viewModel.onChangeLaunchScrollPosition(index)
    }

    private fun startForegroundService() {
        val serviceIntent = Intent(requireContext(), CounterService::class.java).apply {
            action = CounterService.START_SERVICE_ACTION_CODE
        }
        serviceIntent.putExtra("inputExtra", "A message toForeground Service")
        requireContext().startService(serviceIntent)
    }

    private fun stopForegroundService() {
        val serviceIntent = Intent(requireContext(), CounterService::class.java)
        requireContext().stopService(serviceIntent)
    }
}