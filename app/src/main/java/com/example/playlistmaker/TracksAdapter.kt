package com.example.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TracksAdapter(private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<TracksViewHolder> () {

    var tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder = TracksViewHolder(parent)

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(tracks[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = tracks.size
}

