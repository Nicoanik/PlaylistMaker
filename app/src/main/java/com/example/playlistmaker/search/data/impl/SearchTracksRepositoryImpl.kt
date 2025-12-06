package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.domain.models.Resource
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.SearchTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTracksRepositoryImpl(private val networkClient: NetworkClient) :
    SearchTracksRepository {
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
