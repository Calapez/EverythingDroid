package com.brunoponte.everythingdroid.ui.dashboard.list_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.brunoponte.everythingdroid.databinding.DashboardListItemBinding
import com.brunoponte.everythingdroid.enums.DashboardOption


interface DashboardListInteraction {
    fun onClick(position: Int, dashboardOption: DashboardOption)
}

class DashboardListAdapter(
    private val interaction: DashboardListInteraction
) : ListAdapter<DashboardOption, DashboardViewHolder>(DashboardListAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val itemBinding = DashboardListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DashboardViewHolder(itemBinding, interaction)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    private companion object : DiffUtil.ItemCallback<DashboardOption>() {

        override fun areItemsTheSame(oldItem: DashboardOption, newItem: DashboardOption): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DashboardOption, newItem: DashboardOption): Boolean {
            return oldItem == newItem
        }
    }
}