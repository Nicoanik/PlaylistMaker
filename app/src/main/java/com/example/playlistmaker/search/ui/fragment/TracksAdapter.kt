package com.example.playlistmaker.search.ui.fragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.media.domain.models.Track

class TracksAdapter(
    val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<TracksViewHolder>() {

    val tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder =
        TracksViewHolder(parent)

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(tracks[position])
        }
    }

    override fun getItemCount(): Int = tracks.size

    fun interface OnItemClickListener {
        fun onItemClick(track: Track)
    }
}
