package com.example.android.investaxchange.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.investaxchange.R
import com.example.android.investaxchange.data.Asset
import com.example.android.investaxchange.data.GitHubRepo

class BookmarkFragment : Fragment(R.layout.bookmarked_repos) {
    private val repoListAdapter = AssetListAdapter(::onAssetClick)
    private lateinit var bookmarkedReposRV: RecyclerView

    private val viewModel: BookmarkViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookmarkedReposRV = view.findViewById(R.id.rv_bookmarked_repos)
        bookmarkedReposRV.layoutManager = LinearLayoutManager(requireContext())
        bookmarkedReposRV.setHasFixedSize(true)
        bookmarkedReposRV.adapter = this.repoListAdapter

//        viewModel.bookmarkedRepos.observe(viewLifecycleOwner) { bookmarkedRepos ->
//            repoListAdapter.updateRepoList(bookmarkedRepos)
//        }
    }

    private fun onAssetClick(asset: Asset) {
//        val directions = BookmarkedReposFragmentDirections.navigateToRepoDetail(repo, 32)
//        findNavController().navigate(directions)
    }
}