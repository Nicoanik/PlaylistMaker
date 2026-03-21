package com.example.playlistmaker.domain.sharing.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.services.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.models.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareMessage(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.share_text)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            context.getString(R.string.my_mail),
            context.getString(R.string.mail_subject),
            context.getString(R.string.mail_message)
        )
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.agreement_url)
    }
}
