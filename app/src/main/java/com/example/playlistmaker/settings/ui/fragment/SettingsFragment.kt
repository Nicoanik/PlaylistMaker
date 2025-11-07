package com.example.playlistmaker.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel by viewModel<SettingsViewModel>()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            binding.themeSwitcher.isChecked = it
        }

        viewModel.getSetSwitcher()

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            if (switcher.isPressed) {
                viewModel.changeThemeMode(checked)
            }
        }

        binding.shareButtonSettings.setOnClickListener{
            viewModel.shareApp()
        }

        binding.supportButtonSettings.setOnClickListener{
            viewModel.openSupport()
        }

        binding.agreementButtonSettings.setOnClickListener{
            viewModel.openTerms()
        }
    }
}
