package com.example.playlistmaker.settings.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by viewModels()

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.observeState().observe(this) {
            binding.themeSwitcher.isChecked = it
        }

        viewModel.getSetSwitcher()

        binding.backButtonSettings.setOnClickListener{
            finish()
        }

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
