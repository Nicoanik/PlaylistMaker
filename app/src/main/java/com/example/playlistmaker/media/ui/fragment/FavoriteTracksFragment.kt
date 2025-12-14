package com.example.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.media.ui.view_model.FavoriteState
import com.example.playlistmaker.media.ui.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.fragment.TracksAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment: Fragment() {

    private val favoriteViewModel: FavoriteTracksViewModel by viewModel()

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterFavorite: FavoriteTracksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteViewModel.state().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: FavoriteState) {
        when(state) {
            is FavoriteState.Empty -> showPlaceholder()
            is FavoriteState.Content -> showContent(state.tracks)
        }
    }

    private fun showPlaceholder() {
        binding.apply {
            ivPlaceholderImage.isVisible = true
            tvPlaceholderMessage.isVisible = true
        }
    }
    private fun showContent(tracks: List<Track>) {
        binding.apply {
            ivPlaceholderImage.isVisible = false
            tvPlaceholderMessage.isVisible = false
        }
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}
