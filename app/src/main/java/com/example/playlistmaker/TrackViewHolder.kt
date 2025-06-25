package com.example.playlistmaker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackNameView: TextView = itemView.findViewById(R.id.track_name)
    private val artistNameView: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTimeView: TextView = itemView.findViewById(R.id.track_time)
    private val artworkUrl10View: TextView = itemView.findViewById(R.id.album_cover)

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = model.trackTime
        artworkUrl10View.text = model.artworkUrl100
    }
}