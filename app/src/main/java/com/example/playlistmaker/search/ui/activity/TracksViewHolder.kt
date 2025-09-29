package com.example.playlistmaker.search.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.dpToPx
import com.example.playlistmaker.search.domain.models.timeConversion

class TracksViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.track_view, parent, false)) {

    private val trackNameView: TextView = itemView.findViewById(R.id.track_name)
    private val artistNameView: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTimeView: TextView = itemView.findViewById(R.id.tv_track_time)
    private val artworkUrl100View: ImageView = itemView.findViewById(R.id.album_cover)

    fun bind(track: Track) {
        trackNameView.text = track.trackName
        artistNameView.text = track.artistName
        trackTimeView.text = timeConversion(track.trackTime)
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.album_cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2, itemView.context)))
            .into(artworkUrl100View)
    }
}
