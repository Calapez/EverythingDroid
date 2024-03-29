package com.brunoponte.everythingdroid.ui.githubRepos.listAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brunoponte.everythingdroid.databinding.RepoItemBinding
import com.brunoponte.everythingdroid.domain.RepoItem
import com.squareup.picasso.Picasso

class RepoListAdapter: ListAdapter<RepoItem, RepoListAdapter.ViewHolder>(RepoListAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RepoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindRepo(getItem(position))
    }

    class ViewHolder(
        val binding: RepoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindRepo(repo: RepoItem) {
            binding.username.text = repo.owner.login.orEmpty()
            binding.repoName.text = repo.fullName.orEmpty()
            binding.repoDescription.text = repo.description.orEmpty()
            Picasso.get().load(repo.owner.avatarUrl).into(binding.icon)
        }
    }

    private companion object : DiffUtil.ItemCallback<RepoItem>() {

        override fun areItemsTheSame(oldItem: RepoItem, newItem: RepoItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RepoItem, newItem: RepoItem): Boolean {
            return oldItem == newItem
        }
    }
}