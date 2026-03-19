package com.example.playlistmaker.ui.fragments.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.ui.view_models.media.FavoriteState
import com.example.playlistmaker.ui.view_models.media.FavoriteTracksViewModel
import com.example.playlistmaker.ui.fragments.player.PlayerFragment
import com.example.playlistmaker.domain.media.models.Track
import com.example.playlistmaker.utils.antiRepetition
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    private val favoriteViewModel: FavoriteTracksViewModel by viewModel()

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private lateinit var adapterFavorite: FavoriteTracksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteViewModel.getFavorites()

        onTrackClickDebounce = antiRepetition(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope
        ) { track ->
            findNavController().navigate(
                R.id.action_mediaFragment_to_playerFragment,
                PlayerFragment.createArgs(track)
            )
        }

        adapterFavorite = FavoriteTracksAdapter { track -> onTrackClickDebounce(track) }
        binding.rvTracksList.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvTracksList.adapter = adapterFavorite

        favoriteViewModel.state().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: FavoriteState) {
        when (state) {
            is FavoriteState.Empty -> showPlaceholder()
            is FavoriteState.Content -> showContent(state.tracks)
        }
    }

    private fun showPlaceholder() {
        binding.apply {
            rvTracksList.isVisible = false
            ivPlaceholderImage.isVisible = true
            tvPlaceholderMessage.isVisible = true
        }
    }

    private fun showContent(tracks: List<Track>) {
        adapterFavorite.tracks.clear()
        adapterFavorite.tracks.addAll(tracks)
        adapterFavorite.notifyDataSetChanged()
        binding.apply {
            ivPlaceholderImage.isVisible = false
            tvPlaceholderMessage.isVisible = false
            rvTracksList.isVisible = true
        }
    }

    companion object {

        fun newInstance() = FavoriteTracksFragment()

        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
