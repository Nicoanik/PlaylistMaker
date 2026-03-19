package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import com.example.playlistmaker.data.search.dto.TracksSearchResponse
import com.example.playlistmaker.domain.media.models.Resource
import com.example.playlistmaker.domain.media.models.Track
import com.example.playlistmaker.domain.search.SearchTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTracksRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchTracksRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            -1 -> emit(Resource.Error(response.resultCode.toString()))
            200 -> with(response as TracksSearchResponse) {
                emit(
                    Resource.Success(
                        results.map {
                            Track(
                                it.trackId,
                                it.trackName,
                                it.artistName,
                                it.trackTime,
                                it.collectionName,
                                it.releaseDate,
                                it.primaryGenreName,
                                it.country,
                                it.artworkUrl100,
                                it.previewUrl
                            )
                        }
                    )
                )
            }

            else -> emit(Resource.Error(response.resultCode.toString()))
        }
    }
}
