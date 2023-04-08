package com.brunoponte.everythingdroid.ui.githubRepos

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brunoponte.everythingdroid.databinding.FragmentGithubReposBinding
import com.brunoponte.everythingdroid.domain.RepoResult
import com.brunoponte.everythingdroid.ui.githubRepos.listAdapter.RepoListAdapter
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class GithubReposFragment : Fragment() {
    private lateinit var binding: FragmentGithubReposBinding
    private val listAdapter = RepoListAdapter().apply {
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
        binding = FragmentGithubReposBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.repoList.let { repoList ->
            repoList.layoutManager = LinearLayoutManager(context)
            repoList.adapter = listAdapter
        }

        binding.refreshButton.setOnClickListener {
            if (isNetworkConnected()) {
                fetchGithubRepos()
            } else {
                AlertDialog.Builder(requireContext()).setTitle("No Internet Connection")
                    .setMessage("Please check your internet connection and try again")
                    .setPositiveButton(android.R.string.ok) { _, _ -> }
                    .setIcon(android.R.drawable.ic_dialog_alert).show()
            }
        }
    }

    private fun fetchGithubRepos() {
        val url = "https://api.github.com/search/repositories?q=mario+language:kotlin&sort=start&order=desc"

        CoroutineScope(Dispatchers.IO).launch {
            val repoListJsonStr = URL(url).readText()
            val repoResults = Gson().fromJson(repoListJsonStr, RepoResult::class.java)

            CoroutineScope(Dispatchers.Main).launch {
                listAdapter.submitList(repoResults.items)
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Toast.makeText(requireContext(), "This can't be done with current version of Android", Toast.LENGTH_SHORT).show()
            return true
        }

        //1
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //2
        val activeNetwork = connectivityManager.activeNetwork
        //3
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        //4
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}