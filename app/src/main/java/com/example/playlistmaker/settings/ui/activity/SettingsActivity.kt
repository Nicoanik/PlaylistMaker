package com.example.playlistmaker.settings.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.App

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val settingsAppInteractor = Creator.provideSettingsInteractor()

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

        binding.backButtonSettings.setOnClickListener{
            finish()
        }

        setSwitcher()
        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            if (switcher.isPressed) {
                (applicationContext as App).switchTheme(checked)
                settingsAppInteractor.saveSettingsThemeMode(checked)
            }
            setSwitcher()
        }

        binding.shareButtonSettings.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
            startActivity(shareIntent)
        }

        binding.supportButtonSettings.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = "mailto:".toUri()
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_mail)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_message))
            startActivity(supportIntent)
        }

        binding.agreementButtonSettings.setOnClickListener{
            val address = getString(R.string.agreement_url).toUri()
            val agreementIntent = Intent(Intent.ACTION_VIEW, address)
            startActivity(agreementIntent)
        }
    }

    private fun setSwitcher() {
        if ((applicationContext as App).themeModeKeyActive) {
            binding.themeSwitcher.isChecked = (applicationContext as App).darkTheme
        } else {
            binding.themeSwitcher.isChecked = (applicationContext as App).checkThemeMode()
        }
    }
}
