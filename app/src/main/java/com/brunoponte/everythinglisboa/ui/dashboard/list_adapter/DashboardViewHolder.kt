package com.brunoponte.everythinglisboa.ui.dashboard.list_adapter

import androidx.recyclerview.widget.RecyclerView
import com.brunoponte.everythinglisboa.databinding.DashboardListItemBinding
import com.brunoponte.everythinglisboa.enums.DashboardOption

class DashboardViewHolder(
    private val binding: DashboardListItemBinding,
    private val interaction: DashboardListInteraction
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(dashboardOption: DashboardOption, position: Int) {
        binding.apply {
            textView.text = dashboardOption.text

            cardView.setOnClickListener {
                interaction.onClick(position, dashboardOption)
            }
        }
    }
}