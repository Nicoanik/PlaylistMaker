package com.example.playlistmaker.ui.fragments.playlist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.media.models.Track

class PlaylistAdapter(
    private val onClick: (Track) -> Unit,
    private val onLongClick: (Track) -> Unit
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    val tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder =
        PlaylistViewHolder(parent)

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onClick(tracks[position])
        }
        holder.itemView.setOnLongClickListener {
            onLongClick(tracks[position])
            true
        }
    }

    override fun getItemCount(): Int = tracks.size

}