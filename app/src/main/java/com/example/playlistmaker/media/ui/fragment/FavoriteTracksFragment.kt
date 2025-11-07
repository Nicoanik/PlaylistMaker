package com.example.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.media.ui.view_model.MediaState
import com.example.playlistmaker.media.ui.view_model.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment: Fragment() {

    private val favoriteViewModel: FavoriteTracksViewModel by viewModel()

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

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

        favoriteViewModel.observeState().observe(viewLifecycleOwner) {
            renderSearch(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderSearch(state: MediaState) {
        when(state) {
            is MediaState.Empty -> showPlaceholder()
            is MediaState.NotEmpty -> showContent()
        }
    }

    private fun showPlaceholder() {
        binding.apply {
            ivPlaceholderImage.isVisible = true
            tvPlaceholderMessage.isVisible = true
        }
    }
    private fun showContent() {
        binding.apply {
            ivPlaceholderImage.isVisible = false
            tvPlaceholderMessage.isVisible = false
        }
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}
