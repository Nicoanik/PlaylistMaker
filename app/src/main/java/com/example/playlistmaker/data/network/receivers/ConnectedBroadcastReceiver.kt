package com.example.playlistmaker.data.network.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.playlistmaker.utils.isConnected

internal class ConnectedBroadcastReceiver : BroadcastReceiver() {

    var onNetworkChange: ((Boolean) -> Unit)? = null
    private var isConnected: Boolean? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                if (isConnected == true && intent.getBooleanExtra("state", false)) {
                    chekConnection(context)
                }
            }

            "android.net.conn.CONNECTIVITY_CHANGE" -> chekConnection(context)
        }
    }

    private fun chekConnection(context: Context?) {
        isConnected = isConnected(context)
        isConnected?.let {
            if (!it) onNetworkChange?.invoke(false)
        }
    }

}