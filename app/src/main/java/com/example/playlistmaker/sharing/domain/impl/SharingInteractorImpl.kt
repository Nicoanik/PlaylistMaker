package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.sharing.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {

    val app = Creator.provideApplication()

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return app.getString(R.string.share_text)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            app.getString(R.string.my_mail),
            app.getString(R.string.mail_subject),
            app.getString(R.string.mail_message)
        )
    }

    private fun getTermsLink(): String {
        return app.getString(R.string.agreement_url)
    }
}
