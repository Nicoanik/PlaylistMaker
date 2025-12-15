package com.example.playlistmaker.media.ui.fragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.fragment.TracksViewHolder

class FavoriteTracksAdapter(
    val onItemClickListener: OnItemClickListener
): RecyclerView.Adapter<FavoriteTracksViewHolder> () {

    val tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTracksViewHolder =
        FavoriteTracksViewHolder(parent)

    override fun onBindViewHolder(holder: FavoriteTracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(tracks[position])
        }
    }

    override fun getItemCount(): Int = tracks.size
}

fun interface OnItemClickListener {
    fun onItemClick(track: Track)
}
