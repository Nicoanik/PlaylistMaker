package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class RetrofitNetworkClient(
    private val context: Context,
    private val itunesService: ItunesApiService
): NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        if (dto !is TracksSearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatcher.IO)

//        dto is TracksSearchRequest {
//            try {
//                val response = itunesService.searchTracks(dto.expression)
//                return response.apply { resultCode = 200 }
//            } catch (ex: Exception) {
//                return Response().apply { resultCode = 500 }
//            }
//        } else {
//
//        }


    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}
