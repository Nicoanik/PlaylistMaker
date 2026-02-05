package com.example.playlistmaker.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

internal class ConnectedBroadcastReceiver : BroadcastReceiver() {

    private var isConnected: Boolean? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                if (isConnected == true && intent.getBooleanExtra("state", false)) {
                    showMassage(context)
                }
            }
            "android.net.conn.CONNECTIVITY_CHANGE" -> showMassage(context)
        }
    }

    private fun showMassage(context: Context?) {
        isConnected = isConnected(context)
        isConnected?.let {
            if (!it) {
                Toast.makeText(context, "Отсутствует подключение к интернету", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

}