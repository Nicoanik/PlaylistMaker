package com.example.playlistmaker.ui.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.OnItemClickListener
import com.example.playlistmaker.domain.models.Track

class TracksAdapter(private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<TracksViewHolder> () {

    var tracks: List<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder = TracksViewHolder(parent)

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(tracks[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = tracks.size
}
