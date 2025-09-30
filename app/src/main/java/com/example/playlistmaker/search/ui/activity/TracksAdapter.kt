package com.example.playlistmaker.search.ui.activity

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.search.domain.models.Track

class TracksAdapter(private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<TracksViewHolder> () {

    var tracks: List<Track> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder = TracksViewHolder(parent)

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(tracks[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = tracks.size
}

interface OnItemClickListener {
    fun onItemClick(track: Track)
}
