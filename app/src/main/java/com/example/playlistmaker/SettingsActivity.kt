package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.back_button_settings)
        backButton.setOnClickListener{
            finish()
        }

        val shareButton = findViewById<TextView>(R.id.share_button_settings)
        shareButton.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
            startActivity(shareIntent)
        }

        val supportButton = findViewById<TextView>(R.id.support_button_settings)
        supportButton.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = "mailto:".toUri()
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_mail)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_message))
            startActivity(supportIntent)
        }

        val agreementButton = findViewById<TextView>(R.id.agreement_button_settings)
        agreementButton.setOnClickListener{
            val address = getString(R.string.agreement_url).toUri()
            val agreementIntent = Intent(Intent.ACTION_VIEW, address)
            startActivity(agreementIntent)
        }
    }
}