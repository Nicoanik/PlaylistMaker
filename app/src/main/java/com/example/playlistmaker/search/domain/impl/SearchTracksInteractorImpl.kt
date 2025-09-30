package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.SearchTracksInteractor
import com.example.playlistmaker.search.domain.SearchTracksRepository
import com.example.playlistmaker.search.domain.models.Resource
import java.util.concurrent.Executors

class SearchTracksInteractorImpl(private val repository: SearchTracksRepository) :
    SearchTracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: SearchTracksInteractor.TracksConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(expression)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
            }
        }
    }
}
