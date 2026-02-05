package com.example.playlistmaker.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

internal class ConnectedBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_AIRPLANE_MODE_CHANGED,
            "android.net.conn.CONNECTIVITY_CHANGE" -> {
                if (!isConnected(context)) Toast.makeText(
                    context, "Отсутствует подключение к интернету",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}