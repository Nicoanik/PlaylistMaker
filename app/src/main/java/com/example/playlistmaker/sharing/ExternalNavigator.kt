package com.example.playlistmaker.sharing

import android.content.Intent
import androidx.core.net.toUri
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigator {

    val app = Creator.provideApplication()

    fun shareLink(url: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        app.startActivity(shareIntent)
    }

    fun openLink(url: String) {
        val agreementIntent = Intent(Intent.ACTION_VIEW, url.toUri())
        agreementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        app.startActivity(agreementIntent)
    }

    fun openEmail(email: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = "mailto:".toUri()
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email.email))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, email.subject)
        supportIntent.putExtra(Intent.EXTRA_TEXT, email.message)
        supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        app.startActivity(supportIntent)
    }
}
