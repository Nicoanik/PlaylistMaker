package com.example.playlistmaker.main.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentMainBinding
import com.example.playlistmaker.media.ui.activity.MediaActivity
import com.example.playlistmaker.search.ui.activity.SearchActivity
import com.example.playlistmaker.settings.ui.activity.SettingsActivity

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchButton.setOnClickListener {
            val searchIntent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(searchIntent)
        }

        binding.mediaButton.setOnClickListener {
            val mediaIntent = Intent(requireContext(), MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        binding.settingsButton.setOnClickListener {
            val settingsIntent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
