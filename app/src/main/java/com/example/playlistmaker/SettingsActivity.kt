package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.App.Companion.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.App.Companion.THEME_MODE_KEY
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var themeSwitcher: SwitchMaterial
    private lateinit var shareButton: TextView
    private lateinit var supportButton: TextView
    private lateinit var agreementButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        backButton = findViewById(R.id.back_button_settings)
        themeSwitcher = findViewById(R.id.themeSwitcher)
        shareButton = findViewById(R.id.share_button_settings)
        supportButton = findViewById(R.id.support_button_settings)
        agreementButton = findViewById(R.id.agreement_button_settings)

        backButton.setOnClickListener{
            finish()
        }

        setSwitcher()
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            if (switcher.isPressed) {
                (applicationContext as App).switchTheme(checked)
                val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
                sharedPrefs.edit { putBoolean(THEME_MODE_KEY, checked) }
            }
            setSwitcher()
        }

        shareButton.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
            startActivity(shareIntent)
        }

        supportButton.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = "mailto:".toUri()
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_mail)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_message))
            startActivity(supportIntent)
        }

        agreementButton.setOnClickListener{
            val address = getString(R.string.agreement_url).toUri()
            val agreementIntent = Intent(Intent.ACTION_VIEW, address)
            startActivity(agreementIntent)
        }
    }

    private fun setSwitcher() {
        if ((applicationContext as App).themeModeKeyActive) {
            themeSwitcher.isChecked = (applicationContext as App).darkTheme
        } else {
            themeSwitcher.isChecked = (applicationContext as App).checkThemeMode()
        }
    }
}