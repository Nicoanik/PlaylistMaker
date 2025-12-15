package com.example.playlistmaker.search.ui.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.ui.fragment.PlayerFragment
import com.example.playlistmaker.search.ui.view_model.SearchState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchFragment : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private lateinit var adapterSearch: TracksAdapter
    private lateinit var adapterHistory: TracksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            viewModel.addTrackToSearchHistory(track)
            findNavController().navigate(R.id.action_searchFragment_to_playerFragment,
                PlayerFragment.createArgs(track))
        }

        adapterSearch = TracksAdapter { track -> onTrackClickDebounce(track) }
        binding.rvTracksList.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvTracksList.adapter = adapterSearch

        adapterHistory = TracksAdapter { track -> onTrackClickDebounce(track) }
        binding.rvSearchHistory.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvSearchHistory.adapter = adapterHistory

        viewModel.observeState().observe(viewLifecycleOwner) {
            renderSearch(it)
        }

        viewModel.observeShowToast().observe(viewLifecycleOwner) {
            showToast(it.toString())
        }

        binding.clearSearchHistoryButton.setOnClickListener {
            viewModel.clearSearchHistory()
            binding.vgSearchHistory.isVisible = false
        }

        binding.etQueryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchRequest(binding.etQueryInput.text.toString())
                true
            }
            false
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    viewModel.getSearchHistory()
                } else{
                    binding.vgSearchHistory.isVisible = false
                }
                binding.clearButton.isVisible = !s.isNullOrEmpty()
                viewModel.searchDebounce(s?.toString() ?: TEXT_DEF)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.etQueryInput.addTextChangedListener(textWatcher)

        binding.clearButton.setOnClickListener {
            placeholderInvisible()
            binding.etQueryInput.setText(TEXT_DEF)
            binding.rvTracksList.isVisible = false
            binding.vgSearchHistory.isVisible = (adapterHistory.tracks.isNotEmpty())
        }

        binding.rvTracksList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    binding.etQueryInput.clearFocus()
                    val imm = binding.etQueryInput.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.etQueryInput.windowToken, 0)
                }
            }
        })

        binding.refreshButtonSearch.setOnClickListener {
            placeholderInvisible()
            viewModel.searchRequest(binding.etQueryInput.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun placeholderInvisible() {
        binding.apply {
            tvPlaceholderMessage.isVisible = false
            ivPlaceholderErrorImage.isVisible = false
            ivPlaceholderEmptyImage.isVisible = false
            refreshButtonSearch.isVisible = false
        }
    }

    private fun showLoading() {
        binding.apply {
            rvTracksList.isVisible = false
            tvPlaceholderMessage.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun showError() {
        binding.apply {
            progressBar.isVisible = false
            vgSearchHistory.isVisible = false
            rvTracksList.isVisible = false
            tvPlaceholderMessage.text = getString(R.string.something_went_wrong)
            tvPlaceholderMessage.isVisible = true
            ivPlaceholderErrorImage.isVisible = true
            refreshButtonSearch.isVisible = true
        }
    }

    private fun showContent(tracks: List<Track>) {
        adapterSearch.tracks.clear()
        adapterSearch.tracks.addAll(tracks)
        adapterSearch.notifyDataSetChanged()
        placeholderInvisible()
        binding.apply {
            progressBar.isVisible = false
            vgSearchHistory.isVisible = false
            rvTracksList.isVisible = true
        }
    }

    private fun showEmpty() {
        binding.apply {
            progressBar.isVisible = false
            vgSearchHistory.isVisible = false
            rvTracksList.isVisible = false
            tvPlaceholderMessage.text = getString(R.string.nothing_found)
            tvPlaceholderMessage.isVisible = true
            ivPlaceholderEmptyImage.isVisible = true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
    private fun showHistory(tracksHistory: List<Track>) {
        if (tracksHistory.isNotEmpty()) {
            adapterHistory.tracks.clear()
            adapterHistory.tracks.addAll(tracksHistory)
            adapterHistory.notifyDataSetChanged()
            placeholderInvisible()
            binding.apply {
                progressBar.isVisible = false
                rvTracksList.isVisible = false
                vgSearchHistory.isVisible = true
            }
        }
    }

    fun renderSearch(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Error -> showError()
            is SearchState.Empty -> showEmpty()
            is SearchState.History -> showHistory(state.tracksHistory)
        }
    }

    companion object {
        const val TEXT_DEF = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
