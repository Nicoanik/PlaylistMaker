package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.SearchTracksInteractor
import com.example.playlistmaker.search.data.SearchTracksRepository
import java.util.concurrent.Executors

class SearchTracksInteractorImpl(private val repository: SearchTracksRepository) :
    SearchTracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: SearchTracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }
}