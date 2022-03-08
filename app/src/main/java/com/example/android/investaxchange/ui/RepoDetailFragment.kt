package com.example.android.investaxchange.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.android.investaxchange.R
import com.google.android.material.snackbar.Snackbar

const val EXTRA_GITHUB_REPO = "com.example.android.investaxchange.GitHubRepo"

class RepoDetailFragment : Fragment(R.layout.repo_detail) {
    private var isBookmarked = false

    private val args: RepoDetailFragmentArgs by navArgs()

    private val viewModel: BookmarkedReposViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        Log.d("RepoDetailFragment", "args.intval: ${args.intVal}, args.repo: ${args.repo}")

        view.findViewById<TextView>(R.id.tv_repo_name).text = args.repo.name
        view.findViewById<TextView>(R.id.tv_repo_stars).text = args.repo.stars.toString()
        view.findViewById<TextView>(R.id.tv_repo_description).text = args.repo.description
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.activity_repo_detail, menu)
        val bookmarkItem = menu.findItem(R.id.action_bookmark)
        viewModel.getBookmarkedRepoByName(args.repo.name).observe(viewLifecycleOwner) { bookmarkedRepo ->
             when (bookmarkedRepo) {
                null -> {
                    isBookmarked = false
                    bookmarkItem.isChecked = false
                    bookmarkItem.icon = AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_action_bookmark_off
                    )
                }
                else -> {
                    isBookmarked = true
                    bookmarkItem.isChecked = true
                    bookmarkItem.icon = AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_action_bookmark_on
                    )
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_view_repo -> {
                viewRepoOnWeb()
                true
            }
            R.id.action_share -> {
                shareRepo()
                true
            }
            R.id.action_bookmark -> {
                toggleRepoBookmark(item)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * This method toggles the state of the bookmark icon in the top app bar whenever the user
     * clicks it.
     */
    private fun toggleRepoBookmark(menuItem: MenuItem) {
        isBookmarked = !isBookmarked
        when (isBookmarked) {
            true -> {
                viewModel.addBookmarkedRepo(args.repo)
            }
            false -> {
                viewModel.removeBookmarkedRepo(args.repo)
            }
        }
    }

    private fun viewRepoOnWeb() {
        val intent: Intent = Uri.parse(args.repo.url).let {
            Intent(Intent.ACTION_VIEW, it)
        }
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(
                requireView(),
                R.string.action_view_repo_error,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun shareRepo() {
        val text = getString(R.string.share_text, args.repo.name, args.repo.url)
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, null))
    }
}