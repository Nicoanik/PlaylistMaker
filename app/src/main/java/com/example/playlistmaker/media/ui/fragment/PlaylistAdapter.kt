package com.example.playlistmaker.media.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistViewBinding
import com.example.playlistmaker.media.domain.models.Playlist

class PlaylistAdapter(
    val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    val playlists: MutableList<Playlist> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder {
        val binding = PlaylistViewBinding.inflate(LayoutInflater.from(parent.context))
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlaylistViewHolder,
        position: Int
    ) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(playlists[position])
        }
    }

    override fun getItemCount(): Int = playlists.size

    fun interface OnItemClickListener {
        fun onItemClick(playlist: Playlist)
    }
}
