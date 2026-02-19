package com.example.playlistmaker.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.playlistmaker.R

internal class ConnectedBroadcastReceiver : BroadcastReceiver() {

    private var isConnected: Boolean? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                if (isConnected == true && intent.getBooleanExtra("state", false)) {
                    showMessage(context)
                }
            }
            "android.net.conn.CONNECTIVITY_CHANGE" -> showMessage(context)
        }
    }

    private fun showMessage(context: Context?) {
        isConnected = isConnected(context)
        isConnected?.let {
            if (!it) {
                Toast.makeText(context, R.string.no_internet, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

}